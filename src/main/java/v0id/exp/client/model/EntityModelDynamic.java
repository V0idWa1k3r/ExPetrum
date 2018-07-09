package v0id.exp.client.model;

import com.google.common.collect.Lists;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import v0id.api.exp.data.ExPMisc;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public abstract class EntityModelDynamic extends ModelBase
{
    public static final List<EntityModelDynamic> models = Lists.newArrayList();
    public final List<ModelRenderer> components = Lists.newArrayList();
    public static long lastReloadTime = 0L;

    public EntityModelDynamic()
    {
        this.createModel();
        models.add(this);
    }

    public abstract void createModel();

    private static final Field fld_displayList = ReflectionHelper.findField(ModelRenderer.class, "displayList", "field_78811_r");

    public void reloadModel()
    {
        this.boxList.forEach(this::freeGlList);
        this.boxList.clear();
        this.components.clear();
        this.createModel();
    }

    private void freeGlList(ModelRenderer renderer)
    {
        try
        {
            fld_displayList.setAccessible(true);
            GLAllocation.deleteDisplayLists(fld_displayList.getInt(renderer));
        }
        catch (Exception ex)
        {
            ExPMisc.modLogger.error("Could not free GL mem of list for %s at %s!", renderer.toString(), this.toString());
        }
    }

    public static void reloadAllModels()
    {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastReloadTime < 1000)
        {
            lastReloadTime = currentTime;
            return;
        }

        lastReloadTime = currentTime;
        models.forEach(EntityModelDynamic::reloadModel);
        ExPMisc.modLogger.debug("Reloaded all reloadable models!");
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.components.forEach(box -> box.render(scale));
    }
}
