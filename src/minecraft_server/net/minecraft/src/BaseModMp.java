// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            BaseMod, ModLoaderMp, Packet230ModLoader, EntityPlayerMP, 
//            ICommandListener, ConsoleCommandHandler

public abstract class BaseModMp extends BaseMod
{

    public BaseModMp()
    {
    }

    public final int getId()
    {
        return toString().hashCode();
    }

    public void ModsLoaded()
    {
        ModLoaderMp.InitModLoaderMp();
    }

    public void HandlePacket(Packet230ModLoader packet230modloader, EntityPlayerMP entityplayermp)
    {
    }

    public void HandleLogin(EntityPlayerMP entityplayermp)
    {
    }

    public void HandleSendKey(EntityPlayerMP entityplayermp, int i)
    {
    }

    public void GetCommandInfo(ICommandListener icommandlistener)
    {
    }

    public boolean HandleCommand(String s, String s1, ICommandListener icommandlistener, ConsoleCommandHandler consolecommandhandler)
    {
        return false;
    }

    public boolean hasClientSide()
    {
        return true;
    }
}
