package net.minecraft.src.forgetten_sunbeams.event;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.forgetten_sunbeams.FSBlocks;
import net.minecraft.src.forgetten_sunbeams.blocks.NetherReactor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetherReactorAltar {
    public static final int ACTIVE_DURATION_TICKS = 100; 
    public static final int LIGHTNING_DELAY_TICKS = 0;   
    public static final int COOLING_DURATION_TICKS = 24000; 

    private static final class ActiveReactor {
        final World world;
        final int x, y, z;
        int ticksLeft;
        boolean towerBuilt;

        ActiveReactor(World world, int x, int y, int z, int ticksLeft) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.ticksLeft = ticksLeft;
            this.towerBuilt = false;
        }
    }

    private static final class CoolingReactor {
        final World world;
        final int x, y, z;
        int ticksLeft;

        CoolingReactor(World world, int x, int y, int z, int ticksLeft) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.ticksLeft = ticksLeft;
        }
    }

    private static final List<ActiveReactor> activeReactors = new ArrayList<ActiveReactor>();
    private static final List<CoolingReactor> coolingReactors = new ArrayList<CoolingReactor>();

    public static boolean isAltarValid(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) != FSBlocks.netherReactor.blockID) {
            return false;
        }

        if (world.getBlockId(x - 1, y - 1, z - 1) != Block.blockGold.blockID) return false;
        if (world.getBlockId(x + 1, y - 1, z - 1) != Block.blockGold.blockID) return false;
        if (world.getBlockId(x - 1, y - 1, z + 1) != Block.blockGold.blockID) return false;
        if (world.getBlockId(x + 1, y - 1, z + 1) != Block.blockGold.blockID) return false;

        if (world.getBlockId(x, y - 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x - 1, y - 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x + 1, y - 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x, y - 1, z - 1) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x, y - 1, z + 1) != Block.cobblestone.blockID) return false;

        if (world.getBlockId(x - 1, y, z - 1) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x + 1, y, z - 1) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x - 1, y, z + 1) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x + 1, y, z + 1) != Block.cobblestone.blockID) return false;

        if (world.getBlockId(x, y + 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x - 1, y + 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x + 1, y + 1, z) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x, y + 1, z - 1) != Block.cobblestone.blockID) return false;
        if (world.getBlockId(x, y + 1, z + 1) != Block.cobblestone.blockID) return false;

        return true;
    }

    public static void tryActivate(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        if (id != FSBlocks.netherReactor.blockID) return;

        int meta = world.getBlockMetadata(x, y, z);
        if (meta != NetherReactor.STATE_1) return;

        if (!isAltarValid(world, x, y, z)) return;

        world.setBlockMetadataWithNotify(x, y, z, NetherReactor.STATE_2);
        activeReactors.add(new ActiveReactor(world, x, y, z, ACTIVE_DURATION_TICKS));

        System.out.println("[ForgottenSunbeams] Nether Reactor activated at " + x + "," + y + "," + z);
    }

    public static void onWorldTick(World world) {
        tickActiveReactors(world);
        tickCoolingReactors(world);
    }

    private static void tickActiveReactors(World world) {
        if (activeReactors.isEmpty()) return;

        Iterator<ActiveReactor> it = activeReactors.iterator();
        while (it.hasNext()) {
            ActiveReactor r = it.next();
            if (r.world != world) continue;

            r.ticksLeft--;

            if (!r.towerBuilt && r.ticksLeft <= LIGHTNING_DELAY_TICKS) {
                r.towerBuilt = true;
                buildNetherrackTower(world, r.x, r.y, r.z);
                spawnPigZombies(world, r.x, r.y, r.z);
            }

            if (r.ticksLeft > 0) continue;

            int id = world.getBlockId(r.x, r.y, r.z);
            int meta = world.getBlockMetadata(r.x, r.y, r.z);
            if (id != FSBlocks.netherReactor.blockID || meta != NetherReactor.STATE_2) {
                it.remove();
                continue;
            }

            coolingReactors.add(new CoolingReactor(world, r.x, r.y, r.z, COOLING_DURATION_TICKS));
            it.remove();
        }
    }

    private static void tickCoolingReactors(World world) {
        if (coolingReactors.isEmpty()) return;

        Iterator<CoolingReactor> it = coolingReactors.iterator();
        while (it.hasNext()) {
            CoolingReactor r = it.next();
            if (r.world != world) continue;

            r.ticksLeft--;

            int id = world.getBlockId(r.x, r.y, r.z);
            if (id == FSBlocks.netherReactor.blockID) {
                if (r.ticksLeft > 16000) {
                    if (world.getBlockMetadata(r.x, r.y, r.z) != NetherReactor.STATE_2) {
                        world.setBlockMetadataWithNotify(r.x, r.y, r.z, NetherReactor.STATE_2);
                    }
                } else if (r.ticksLeft > 8000) {
                    if (world.getBlockMetadata(r.x, r.y, r.z) != NetherReactor.STATE_1) {
                        world.setBlockMetadataWithNotify(r.x, r.y, r.z, NetherReactor.STATE_1);
                    }
                } else {
                    if (world.getBlockMetadata(r.x, r.y, r.z) != NetherReactor.STATE_3) {
                        world.setBlockMetadataWithNotify(r.x, r.y, r.z, NetherReactor.STATE_3);
                    }
                }
            }

            coolAltarStepByStep(world, r.x, r.y, r.z, r.ticksLeft);

            if (r.ticksLeft > 0) continue;

            it.remove();
        }
    }

    private static void coolAltarStepByStep(World world, int cx, int cy, int cz, int ticksLeft) {
        int passedTicks = COOLING_DURATION_TICKS - ticksLeft;
        int totalBlocksToChange = 9 + 4 + 5; 
        int targetBlocksCount = (passedTicks * totalBlocksToChange) / COOLING_DURATION_TICKS;

        int currentChanged = 0;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    int bx = cx + dx;
                    int by = cy + dy;
                    int bz = cz + dz;
                    int bid = world.getBlockId(bx, by, bz);

                    boolean isAltarPart = false;
                    if (dy == -1) {
                        isAltarPart = (bid == Block.blockGold.blockID || bid == Block.cobblestone.blockID);
                    } else if (dy == 0) {
                        isAltarPart = (Math.abs(dx) == 1 && Math.abs(dz) == 1 && bid == Block.cobblestone.blockID);
                    } else if (dy == 1) {
                        isAltarPart = ((Math.abs(dx) + Math.abs(dz) == 1 || (dx == 0 && dz == 0)) && bid == Block.cobblestone.blockID);
                    }

                    if (isAltarPart || bid == FSBlocks.glowingObsidian.blockID) {
                        if (currentChanged < targetBlocksCount) {
                            if (bid != FSBlocks.glowingObsidian.blockID) {
                                world.setBlockWithNotify(bx, by, bz, FSBlocks.glowingObsidian.blockID);
                            }
                            currentChanged++;
                        }
                    }
                }
            }
        }
    }

    private static void buildNetherrackTower(World world, int x, int y, int z) {
        for (int dy = -4; dy <= -1; dy++) {
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    if (dy == -1 && Math.abs(dx) <= 1 && Math.abs(dz) <= 1) continue;
                    setNetherrack(world, x + dx, y + dy, z + dz);
                }
            }
        }

        for (int dy = 0; dy <= 7; dy++) {
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    if (dy >= 0 && dy <= 2 && Math.abs(dx) <= 1 && Math.abs(dz) <= 1) continue;

                    boolean isWall = Math.abs(dx) >= 6 || Math.abs(dz) >= 6 || dy == 7;
                    if (isWall) {
                        int rand = world.rand.nextInt(100);
                        if (rand < 5) {
                            continue;
                        } else if (rand < 8 && dy > 1 && dy < 7 && (Math.abs(dx) == 8 || Math.abs(dz) == 8)) {
                            world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.lavaMoving.blockID);
                        } else {
                            setNetherrack(world, x + dx, y + dy, z + dz);
                        }
                    }
                }
            }
        }
        
        int randMainType = world.rand.nextInt(4);
        for (int dy = 8; dy <= 20; dy++) {
            int border = -16 + (dy - 8) * 2; 
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    boolean place = false;
                    if (randMainType == 0) {
                        place = (dx + dz >= border);
                    } else if (randMainType == 1) {
                        place = ((-dx) + dz >= border);
                    } else if (randMainType == 2) {
                        place = (dx + (-dz) >= border);
                    } else {
                        place = ((-dx) + (-dz) >= border);
                    }
                    if (place) {
                        setNetherrack(world, x + dx, y + dy, z + dz);
                    }
                }
            }
        }

        for (int dy = 8; dy <= 17; dy++) {
            for (int dx = -6; dx <= 6; dx++) {
                for (int dz = -6; dz <= 6; dz++) {
                    setNetherrack(world, x + dx, y + dy, z + dz);
                }
            }
        }

        int randCenterType = world.rand.nextInt(4);
        for (int dy = 18; dy <= 27; dy++) {
            int border = -12 + (dy - 18);
            for (int dx = -6; dx <= 6; dx++) {
                for (int dz = -6; dz <= 6; dz++) {
                    boolean place = false;
                    if (randCenterType == 0) {
                        place = (dx + dz >= border);
                    } else if (randCenterType == 1) {
                        place = ((-dx) + dz >= border);
                    } else if (randCenterType == 2) {
                        place = (dx + (-dz) >= border);
                    } else {
                        place = ((-dx) + (-dz) >= border);
                    }
                    if (place) {
                        setNetherrack(world, x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }



    private static void setNetherrack(World world, int bx, int by, int bz) {
        int currentID = world.getBlockId(bx, by, bz);
        if (currentID == 0 || currentID == Block.stone.blockID || currentID == Block.blockGold.blockID) {
            world.setBlockWithNotify(bx, by, bz, 87);
        }
    }

    private static void spawnPigZombies(World world, int x, int y, int z) {
        for (int i = 0; i < 8; i++) {
            int spawnX = x + world.rand.nextInt(9) - 4;
            int spawnZ = z + world.rand.nextInt(9) - 4;
            int spawnY = y + 1;

            while (world.getBlockId(spawnX, spawnY, spawnZ) != 0 && spawnY < y + 7) {
                spawnY++;
            }

            EntityPigZombie zombie = new EntityPigZombie(world);
            zombie.setLocationAndAngles(spawnX + 0.5, spawnY, spawnZ + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);
            world.entityJoinedWorld(zombie);
        }
    }

    private static void coolLayer(World world, int x, int y, int z, int layer) {
        int by = y + layer;
        for (int dx = -8; dx <= 8; dx++) {
            for (int dz = -8; dz <= 8; dz++) {
                int bx = x + dx;
                int bz = z + dz;
                int id = world.getBlockId(bx, by, bz);
                if (id == 87 || id == Block.stone.blockID || id == Block.cobblestone.blockID) {
                    world.setBlockWithNotify(bx, by, bz, FSBlocks.glowingObsidian.blockID);
                }
            }
        }
    }

    private static void coolCore(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        if (id == FSBlocks.netherReactor.blockID) {
            world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
        }

        for (int dy = -1; dy <= 1; dy++) {
        	for (int dx = -1; dx <= 1; dx++) {
        		for (int dz = -1; dz <= 1; dz++) {
        			int bx = x + dx;
        			int by = y + dy;
        			int bz = z + dz;int bid = world.getBlockId(bx, by, bz);

        			if (bid == FSBlocks.netherReactor.blockID || bid == 87) {
                        world.setBlockWithNotify(bx, by, bz, Block.obsidian.blockID);
                    }
                }
            }
        }
    }

    public static void clear() {
        activeReactors.clear();
        coolingReactors.clear();
    }
}