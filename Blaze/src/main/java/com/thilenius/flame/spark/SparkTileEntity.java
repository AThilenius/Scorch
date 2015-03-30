package com.thilenius.flame.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.thilenius.flame.Flame;
import com.thilenius.flame.entity.*;
import com.thilenius.flame.utilities.MathUtils;
import com.thilenius.flame.utilities.types.CountdownTimer;
import com.thilenius.flame.utilities.types.Location3D;
import com.thilenius.flame.utilities.types.LocationF3D;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@FlameEntityDefinition(name = "Spark", blockTextureName = "flame:sparkItem")
public class SparkTileEntity extends FlameTileEntity {

    public enum FaceDirections {
        North,
        East,
        South,
        West
    }
    public enum AnimationTypes {
        Idle,
        TurnLeft,
        TurnRight,
        Forward,
        Backward,
        Up,
        Down
    }

    public final static float ANIMATION_TIME = 0.5f;
    private static ModelSparkSmall s_model = new ModelSparkSmall();
    private static Block s_sparkBlock;

    private FaceDirections m_currentFaceDir = FaceDirections.North;
    private AnimationTypes m_currentAnimation = AnimationTypes.Idle;
    private CountdownTimer m_animationTimer;


    // ======   FlameTileEntity Overrides   ============================================================================
    @FlameEntityInitializer
    public static void entityInitialize(Block block) {
        s_sparkBlock = block;
        GameRegistry.addRecipe(new ItemStack(block), new Object[]{
                "BAB",
                "AAA",
                "BAB",
                'A', Blocks.cobblestone, 'B', Blocks.dirt
        });
    }

    @FlameCustomRenderer
    public static void render (FlameSupportRenderer renderer, FlameTileEntity tileEntity, LocationF3D location) {
        SparkTileEntity spark = (SparkTileEntity) tileEntity;
        float fractionTime = spark.m_animationTimer != null ? spark.m_animationTimer.getRemainingRatio() : -1.0f;

        // Compute Rotation
        float rotation = 0.0f;
        switch (spark.m_currentFaceDir) {
            case North: rotation = 0.0f; break;
            case East: rotation = 90.0f; break;
            case South: rotation = 180.0f; break;
            case West: rotation = 270.0f; break;
        }

        if (fractionTime > 0.0f) {
            switch (spark.m_currentAnimation) {
                case TurnLeft: rotation += MathUtils.lerp(0.0f, 90.0f, fractionTime); break;
                case TurnRight: rotation += MathUtils.lerp(0.0f, -90.0f, fractionTime); break;
            }
        }

        // Compute Offset
        LocationF3D offset = new LocationF3D();
        if (fractionTime > 0.0f) {
            // Moving forward backward
            if (spark.m_currentAnimation == AnimationTypes.Forward ||
                    spark.m_currentAnimation == AnimationTypes.Backward) {
                LocationF3D facingDirection = spark.getRotationVector();
                // Reverse the rotation vector, because we are animating backward
                facingDirection = facingDirection.scale(-1.0f);
                if (spark.m_currentAnimation == AnimationTypes.Backward) {
                    facingDirection = facingDirection.scale(-1.0f);
                }
                offset = facingDirection.scale(fractionTime);
            } else if (spark.m_currentAnimation == AnimationTypes.Up ||
                    spark.m_currentAnimation == AnimationTypes.Down) {
                float up = spark.m_currentAnimation == AnimationTypes.Up ? -1.0f : 1.0f;
                offset = new LocationF3D(0.0f, up, 0.0f).scale(fractionTime);
            }
        }

        // Draw
        GL11.glPushMatrix();
        GL11.glTranslatef((float) location.X + 0.5f + offset.X,
                          (float) location.Y + 0.35f + offset.Y,
                          (float) location.Z + 0.6f + offset.Z);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
        ResourceLocation textures = (new ResourceLocation("flame:textures/model/Spark.png"));
        renderer.bindTexture(textures);
        s_model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    protected void onBlockAdded(World world, int x, int y, int z) {
        FlameSupportGuiHandler.ActiveEntity = this;
        FlameSupportGuiHandler.ActiveLocation = new Location3D(x, y, z);
        Minecraft.getMinecraft().thePlayer.openGui(Flame.instance, 0, world, x, y, z);
    }

    @Override
    public void postNamedInit() {
        super.postNamedInit();
        System.out.println("Port Named Init with name " + getName());
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 16384.0D;
    }


    // ======   Network / Disk IO Handling   ===========================================================================
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setString("faceDir", m_currentFaceDir.name());
        nbt.setString("animation", m_currentAnimation.name());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        m_currentFaceDir = FaceDirections.valueOf(nbt.getString("faceDir"));
        m_currentAnimation = AnimationTypes.valueOf(nbt.getString("animation"));
        m_animationTimer = new CountdownTimer(ANIMATION_TIME);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.func_148857_g());
    }


    // ======   Action Path Handlers   =================================================================================
    @FlameActionPath("move")
    public boolean moveAction(JsonNode message) throws FlameActionException {
        AnimationTypes animationType = AnimationTypes.valueOf(message.get("direction").asText());
        if (animationType == null ||
               (animationType != AnimationTypes.Forward &&
                animationType != AnimationTypes.Backward &&
                animationType != AnimationTypes.Up &&
                animationType != AnimationTypes.Down)) {
            throw new FlameActionException("Invalid or missing field [direction]");
        }

        Location3D newLocation = getBlockFromAction(animationType);

        // Check if we can move to the new spot
        if (Flame.Globals.World.isAirBlock(newLocation.X, newLocation.Y, newLocation.Z)) {
            Flame.Globals.World.setBlock(newLocation.X, newLocation.Y, newLocation.Z, s_sparkBlock);
            SparkTileEntity sparkTileEntity = (SparkTileEntity) Flame.Globals.World.getTileEntity(newLocation.X,
                    newLocation.Y, newLocation.Z);
            sparkTileEntity.copyFrom(this);
            sparkTileEntity.animateServerAndSendToClients(animationType);
            Flame.Globals.World.setBlockToAir(xCoord, yCoord, zCoord);
            return true;
        } else {
            return false;
        }
    }

    @FlameActionPath("rotate")
    public void rotateAction(JsonNode message) {

    }


    // ======   Helpers   ==============================================================================================
    private void animateServerAndSendToClients(AnimationTypes animationType) {
        m_currentAnimation = animationType;
        m_currentFaceDir = getNewFaceDirByAnimation(animationType);
        m_animationTimer = new CountdownTimer(ANIMATION_TIME);

        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private LocationF3D getRotationVector() {
        LocationF3D facingDirection = new LocationF3D();
        switch (m_currentFaceDir) {
            case North: facingDirection = new LocationF3D(0.0f, 0.0f, -1.0f); break;
            case East: facingDirection = new LocationF3D(1.0f, 0.0f, 0.0f); break;
            case South: facingDirection = new LocationF3D(0.0f, 0.0f, 1.0f); break;
            case West: facingDirection = new LocationF3D(-1.0f, 0.0f, 0.0f); break;
        }
        return facingDirection;
    }

    private FaceDirections getNewFaceDirByAnimation(AnimationTypes animation) {
        switch (animation) {
            case TurnLeft:
                switch (m_currentFaceDir) {
                    case North: return FaceDirections.West;
                    case East: return FaceDirections.North;
                    case South: return FaceDirections.East;
                    case West: return FaceDirections.South;
                }
                break;
            case TurnRight:
                switch (m_currentFaceDir) {
                    case North: return FaceDirections.East;
                    case East: return FaceDirections.South;
                    case South: return FaceDirections.West;
                    case West: return FaceDirections.North;
                }
                break;
        }
        return m_currentFaceDir;
    }

    private Location3D getBlockFromAction(AnimationTypes animation) {
        // Moving forward backward
        if (animation == AnimationTypes.Forward || animation == AnimationTypes.Backward) {
            // Construct faceDir vector
            LocationF3D facingDirection = getRotationVector();
            if (animation == AnimationTypes.Backward) {
                facingDirection = facingDirection.scale(-1.0f);
            }

            // Multiple each component by fractionTime
            return new Location3D(xCoord + Math.round(facingDirection.X), yCoord + Math.round(facingDirection.Y),
                    zCoord + Math.round(facingDirection.Z));
        } else if (animation == AnimationTypes.Up || animation == AnimationTypes.Down) {
            int up = animation == AnimationTypes.Up ? 1 : -1;
            return new Location3D(xCoord, yCoord + up, zCoord);
        } else {
            return new Location3D(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void copyFrom(Object obj) {
        super.copyFrom(obj);
        SparkTileEntity sparkTileEntity = (SparkTileEntity) obj;
        if (sparkTileEntity == null) {
            return;
        }

        m_currentFaceDir = sparkTileEntity.m_currentFaceDir;
        m_currentAnimation = sparkTileEntity.m_currentAnimation;
        m_animationTimer = sparkTileEntity.m_animationTimer;
    }

}
