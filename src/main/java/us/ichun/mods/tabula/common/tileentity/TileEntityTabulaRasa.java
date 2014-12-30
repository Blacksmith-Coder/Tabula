package us.ichun.mods.tabula.common.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ichun.common.core.EntityHelperBase;
import ichun.common.core.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import us.ichun.mods.tabula.common.Tabula;
import us.ichun.mods.tabula.common.packet.PacketEndSession;

import java.util.ArrayList;

public class TileEntityTabulaRasa extends TileEntity
{
    public int side;
    public String host;
    public ArrayList<String> listeners;

    public TileEntityTabulaRasa()
    {
        host = "";
        listeners = new ArrayList<String>();
    }

    @Override
    public void updateEntity()
    {
        if(!worldObj.isRemote && worldObj.getWorldTime() % 24L == 0 && !host.isEmpty() && FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152612_a(host) == null)
        {
            terminateSession(true);
        }
    }

    public void terminateSession(boolean crashed)
    {
        for(String listener : listeners)
        {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152612_a(listener);
            if(player != null)
            {
                PacketHandler.sendToPlayer(Tabula.channels, new PacketEndSession(host, xCoord, yCoord, zCoord, crashed), player);
            }
        }

        host = "";
        listeners.clear();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("side", side);
        tag.setString("host", host);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        side = tag.getInteger("side");
        host = tag.getString("host");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }
}
