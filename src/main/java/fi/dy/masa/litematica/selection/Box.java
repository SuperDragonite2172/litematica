package fi.dy.masa.litematica.selection;

import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.util.JsonUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.PositionUtils.Corner;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class Box
{
    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos size = BlockPos.ORIGIN;
    private String name = "Unnamed";
    private Corner selectedCorner = Corner.NONE;

    public Box()
    {
    }

    public Box(BlockPos pos1, BlockPos pos2, String name)
    {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.name = name;

        this.updateSize();
    }

    @Nullable
    public BlockPos getPos1()
    {
        return this.pos1;
    }

    @Nullable
    public BlockPos getPos2()
    {
        return this.pos2;
    }

    public BlockPos getSize()
    {
        return this.size;
    }

    public String getName()
    {
        return this.name;
    }

    public Corner getSelectedCorner()
    {
        return this.selectedCorner;
    }

    public void setPos1(@Nullable BlockPos pos)
    {
        this.pos1 = pos;
        this.updateSize();
    }

    public void setPos2(@Nullable BlockPos pos)
    {
        this.pos2 = pos;
        this.updateSize();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSelectedCorner(Corner corner)
    {
        this.selectedCorner = corner;
    }

    public void rotate(Rotation rotation)
    {
        BlockPos pos = PositionUtils.getTransformedBlockPos(this.getSize(), Mirror.NONE, rotation);
        this.setPos2(this.getPos1().add(pos).add(-1, -1, -1));
    }

    public void mirror(Mirror mirror)
    {
        BlockPos pos = PositionUtils.getTransformedBlockPos(this.getSize(), mirror, Rotation.NONE);
        this.setPos2(this.getPos1().add(pos).add(-1, -1, -1));
    }

    private void updateSize()
    {
        if (this.pos1 != null && this.pos2 != null)
        {
            this.size = PositionUtils.getAreaSizeFromRelativeEndPosition(this.pos2.subtract(this.pos1));
        }
        else if (this.pos1 == null && this.pos2 == null)
        {
            this.size = BlockPos.ORIGIN;
        }
        else
        {
            this.size = new BlockPos(1, 1, 1);
        }
    }

    @Nullable
    public static Box fromJson(JsonObject obj)
    {
        if (JsonUtils.hasString(obj, "name"))
        {
            BlockPos pos1 = JsonUtils.blockPosFromJson(obj, "pos1");
            BlockPos pos2 = JsonUtils.blockPosFromJson(obj, "pos2");

            if (pos1 != null || pos2 != null)
            {
                Box box = new Box();
                box.setName(obj.get("name").getAsString());

                if (pos1 != null)
                {
                    box.setPos1(pos1);
                }

                if (pos2 != null)
                {
                    box.setPos2(pos2);
                }

                return box;
            }
        }

        return null;
    }

    @Nullable
    public JsonObject toJson()
    {
        JsonObject obj = new JsonObject();

        if (this.pos1 != null)
        {
            obj.add("pos1", JsonUtils.blockPosToJson(this.pos1));
        }

        if (this.pos2 != null)
        {
            obj.add("pos2", JsonUtils.blockPosToJson(this.pos2));
        }

        obj.add("name", new JsonPrimitive(this.name));

        return this.pos1 != null || this.pos2 != null ? obj : null;
    }
}