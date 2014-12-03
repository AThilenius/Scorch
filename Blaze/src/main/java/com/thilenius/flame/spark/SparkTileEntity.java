package com.thilenius.flame.spark;

import com.thilenius.utilities.types.CountdownTimer;
import com.thilenius.utilities.types.Location3D;
import com.thilenius.utilities.types.LocationF3D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class SparkTileEntity extends TileEntity {

    public final static float ANIMATION_TIME = 0.5f;

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

    // Saved by NBT
    private FaceDirections m_currentFaceDir = FaceDirections.North;
    private AnimationTypes m_currenAnimation = AnimationTypes.Idle;

    // Used only by client
    private CountdownTimer m_animationTimer = null;
	
	static {
        addMapping(SparkTileEntity.class, "Spark");
    }
	
	public SparkTileEntity() {
	}

    public Location3D getBlockFromAction(AnimationTypes animation) {
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

    public float getRotation() {
        float faceOffself = 0.0f;
        switch (m_currentFaceDir) {
            case North:
                faceOffself = 0.0f;
                break;
            case East:
                faceOffself = 90.0f;
                break;
            case South:
                faceOffself = 180.0f;
                break;
            case West:
                faceOffself = 270.0f;
                break;
        }

        float fractionTime = m_animationTimer != null ? m_animationTimer.getRemainingRatio() : -1.0f;

        if (fractionTime < 0.0f) {
            return faceOffself;
        }

        switch (m_currenAnimation) {
            case TurnLeft:
                faceOffself += lerp(0.0f, 90.0f, fractionTime);
                break;
            case TurnRight:
                faceOffself += lerp(0.0f, -90.0f, fractionTime);
                break;
        }

        // Let getOffset handle the m_isAnimating flag
        return faceOffself;
    }

    public void animateClients(AnimationTypes animationType) {
        m_currenAnimation = animationType;
        // Still need to handle rotation on the server side
        rotateByAnimation(animationType);
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public LocationF3D getOffset() {
        float fractionTime = m_animationTimer != null ? m_animationTimer.getRemainingRatio() : -1.0f;

        if (fractionTime < 0.0f) {
            return new LocationF3D();
        }

        // Moving forward backward
        if (m_currenAnimation == AnimationTypes.Forward || m_currenAnimation == AnimationTypes.Backward) {
            LocationF3D facingDirection = getRotationVector();
            // Reverse the rotation vector, because we are animating backward
            facingDirection = facingDirection.scale(-1.0f);
            if (m_currenAnimation == AnimationTypes.Backward) {
                facingDirection = facingDirection.scale(-1.0f);
            }

            // Multiple each component by fractionTime
            return facingDirection.scale(fractionTime);
        } else if (m_currenAnimation == AnimationTypes.Up || m_currenAnimation == AnimationTypes.Down) {
            float up = m_currenAnimation == AnimationTypes.Up ? -1.0f : 1.0f;
            return new LocationF3D(0.0f, up, 0.0f).scale(fractionTime);
        } else {
            return new LocationF3D();
        }
    }

    public LocationF3D getRotationVector() {
        LocationF3D facingDirection = new LocationF3D();
        switch (m_currentFaceDir) {
            case North:
                facingDirection = new LocationF3D(0.0f, 0.0f, -1.0f);
                break;
            case East:
                facingDirection = new LocationF3D(1.0f, 0.0f, 0.0f);
                break;
            case South:
                facingDirection = new LocationF3D(0.0f, 0.0f, 1.0f);
                break;
            case West:
                facingDirection = new LocationF3D(-1.0f, 0.0f, 0.0f);
                break;
        }
        return facingDirection;
    }

    public void copyFrom(SparkTileEntity sparkTileEntity) {
        // Copy things that should persist AFTER the spark has been moved.
        // Aka. don't, copy things like location
        m_currentFaceDir = sparkTileEntity.m_currentFaceDir;
    }

    public boolean isAnimating() {
        float fractionTime = m_animationTimer != null ? m_animationTimer.getRemainingRatio() : -1.0f;
        return fractionTime >= 0.0f;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setString("faceDir", m_currentFaceDir.name());
        nbt.setString("animation", m_currenAnimation.name());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        m_currentFaceDir = FaceDirections.valueOf(nbt.getString("faceDir"));
        animate(AnimationTypes.valueOf(nbt.getString("animation")), ANIMATION_TIME);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }

    private void animate(AnimationTypes animation, float duration) {
        m_currenAnimation = animation;
        m_animationTimer = new CountdownTimer(duration);
    }

    private void rotateByAnimation(AnimationTypes animation) {
        // If rotating, change face dir first
        switch (animation) {
            case TurnLeft:
                switch (m_currentFaceDir) {
                    case North: m_currentFaceDir = FaceDirections.West; break;
                    case East: m_currentFaceDir = FaceDirections.North; break;
                    case South: m_currentFaceDir = FaceDirections.East; break;
                    case West: m_currentFaceDir = FaceDirections.South; break;
                }
                break;
            case TurnRight:
                switch (m_currentFaceDir) {
                    case North: m_currentFaceDir = FaceDirections.East; break;
                    case East: m_currentFaceDir = FaceDirections.South; break;
                    case South: m_currentFaceDir = FaceDirections.West; break;
                    case West: m_currentFaceDir = FaceDirections.North; break;
                }
                break;
        }
    }

    private float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }

    private float nanoToSeconds(long nano) {
        return (float)((double)nano / 1000000000.0);
    }

}