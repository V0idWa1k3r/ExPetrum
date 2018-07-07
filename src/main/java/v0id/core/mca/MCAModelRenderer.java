package v0id.core.mca;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class MCAModelRenderer extends ModelRenderer
{
	private int DDStextureOffsetX;
	private int DDStextureOffsetY;
	private boolean DDScompiled;
	private int DDSdisplayList;
	private Matrix4f rotationMatrix = new Matrix4f();
	private Matrix4f prevRotationMatrix = new Matrix4f();
	private float defaultRotationPointX;
	private float defaultRotationPointY;
	private float defaultRotationPointZ;
	private Matrix4f defaultRotationMatrix = new Matrix4f();
	private Quaternion defaultRotationAsQuaternion;

	public MCAModelRenderer(ModelBase par1ModelBase, String par2Str, int xTextureOffset, int yTextureOffset)
	{
		super(par1ModelBase, par2Str);
		this.setTextureSize(par1ModelBase.textureWidth, par1ModelBase.textureHeight);
		this.setTextureOffset(xTextureOffset, yTextureOffset);
	}

	@Override
	public ModelRenderer setTextureOffset(int par1, int par2)
	{
		this.DDStextureOffsetX = par1;
		this.DDStextureOffsetY = par2;
		return this;
	}

	@Override
	public ModelRenderer addBox(String par1Str, float par2, float par3, float par4, int par5, int par6, int par7)
	{
		par1Str = this.boxName + "." + par1Str;
		this.cubeList.add((new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par2, par3, par4, par5, par6, par7, 0.0F)).setBoxName(par1Str));
		return this;
	}

	@Override
	public ModelRenderer addBox(float par1, float par2, float par3, int par4, int par5, int par6)
	{
		this.cubeList.add(new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par1, par2, par3, par4, par5, par6, 0.0F));
		return this;
	}

	@Override
	public void addBox(float par1, float par2, float par3, int par4, int par5, int par6, float par7)
	{
		this.cubeList.add(new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par1, par2, par3, par4, par5, par6, par7));
	}

    public float[] intoArray(Matrix4f matrix)
    {
        float[] m = new float[16];

        m[0] = matrix.m00;
        m[1] = matrix.m01;
        m[2] = matrix.m02;
        m[3] = matrix.m03;
        m[4] = matrix.m10;
        m[5] = matrix.m11;
        m[6] = matrix.m12;
        m[7] = matrix.m13;
        m[8] = matrix.m20;
        m[9] = matrix.m21;
        m[10] = matrix.m22;
        m[11] = matrix.m23;
        m[12] = matrix.m30;
        m[13] = matrix.m31;
        m[14] = matrix.m32;
        m[15] = matrix.m33;

        return m;
    }

    public boolean isEmptyRotationMatrix(Matrix4f matrix)
    {
        if(matrix.m00 == 1 && matrix.m11 == 1 && matrix.m22 == 1 )
        {
            float[] m = this.intoArray(matrix);
            boolean isEmptyRotationMatrix = true;
            for(int i = 0; i < m.length; i++)
            {
                if(i != 0 && i != 5 && i != 10 && i <= 10)
                {
                    if(m[i] != 0)
                    {
                        isEmptyRotationMatrix = false;
                        break;
                    }
                }
            }

            return isEmptyRotationMatrix;
        }

        return false;
    }

	@Override
	public void render(float par1)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.DDScompiled)
				{
					this.DDScompileDisplayList(par1);
				}

				GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
				int i;
				if (this.isEmptyRotationMatrix(rotationMatrix))
				{
					if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
					{
						GL11.glCallList(this.DDSdisplayList);
						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								this.childModels.get(i).render(par1);
							}
						}
					}
					else
					{
						GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
						GL11.glCallList(this.DDSdisplayList);
						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								this.childModels.get(i).render(par1);
							}
						}

						GL11.glTranslatef(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
					}
				}
				else
				{
					GL11.glPushMatrix();
					GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
					FloatBuffer buf = Utils.makeFloatBuffer(this.intoArray(rotationMatrix));
					GL11.glMultMatrix(buf);
					GL11.glCallList(this.DDSdisplayList);
					if (this.childModels != null)
					{
						for (i = 0; i < this.childModels.size(); ++i)
						{
							this.childModels.get(i).render(par1);
						}
					}

					GL11.glPopMatrix();
				}

				GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
				this.prevRotationMatrix = this.rotationMatrix;
			}
		}
	}

	@Override
	public void renderWithRotation(float par1)
	{
	}

	@Override
	public void postRender(float par1)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.DDScompiled)
				{
					this.DDScompileDisplayList(par1);
				}

				if (this.rotationMatrix.equals(prevRotationMatrix))
				{
					if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
					{
						GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
					}
				}
				else
				{
					GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
					GL11.glMultMatrix(FloatBuffer.wrap(this.intoArray(rotationMatrix)));
				}
			}
		}
	}

	/** Set default rotation point (model with no animations) and set the current rotation point. */
	public void setInitialRotationPoint(float par1, float par2, float par3)
	{
		this.defaultRotationPointX = par1;
		this.defaultRotationPointY = par2;
		this.defaultRotationPointZ = par3;
		this.setRotationPoint(par1, par2, par3);
	}

	/** Set the rotation point*/
	public void setRotationPoint(float par1, float par2, float par3) {
		this.rotationPointX = par1;
		this.rotationPointY = par2;
		this.rotationPointZ = par3;
	}

	/** Reset the rotation point to the default values. */
	public void resetRotationPoint(){
		this.rotationPointX = this.defaultRotationPointX;
		this.rotationPointY = this.defaultRotationPointY;
		this.rotationPointZ = this.defaultRotationPointZ;
	}

	public Vector3f getPositionAsVector()
	{
		return new Vector3f(this.rotationPointX, this.rotationPointY, this.rotationPointZ);
	}

	/** Set rotation matrix setting also an initial default value (model with no animations). */
	public void setInitialRotationMatrix(Matrix4f matrix)
	{
		defaultRotationMatrix = matrix;
		setRotationMatrix(matrix);
		this.defaultRotationAsQuaternion = Utils.getQuaternionFromMatrix(rotationMatrix);
	}

	/** Set the rotation matrix values based on the given matrix. */
	public void setRotationMatrix(Matrix4f matrix)
	{
		rotationMatrix.m00 = matrix.m00;
		rotationMatrix.m01 = matrix.m01;
		rotationMatrix.m02 = matrix.m02;
		rotationMatrix.m03 = matrix.m03;
		rotationMatrix.m10 = matrix.m10;
		rotationMatrix.m11 = matrix.m11;
		rotationMatrix.m12 = matrix.m12;
		rotationMatrix.m13 = matrix.m13;
		rotationMatrix.m20 = matrix.m20;
		rotationMatrix.m21 = matrix.m21;
		rotationMatrix.m22 = matrix.m22;
		rotationMatrix.m23 = matrix.m23;
		rotationMatrix.m30 = matrix.m30;
		rotationMatrix.m31 = matrix.m31;
		rotationMatrix.m32 = matrix.m32;
		rotationMatrix.m33 = matrix.m33;
	}

	/** Reset the rotation matrix to the default one. */
	public void resetRotationMatrix() {
		setRotationMatrix(this.defaultRotationMatrix);
	}

	public Matrix4f getRotationMatrix() {
		return this.rotationMatrix;
	}

	public Quaternion getDefaultRotationAsQuaternion() {
		return defaultRotationAsQuaternion;
	}

	/**
	 * Compiles a GL display list for this model.
	 * EDITED VERSION BECAUSE OF THE PRIVATE FIELDS
	 */
	public void DDScompileDisplayList(float par1)
	{
		this.DDSdisplayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(this.DDSdisplayList, GL11.GL_COMPILE);
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		for (int i = 0; i < this.cubeList.size(); ++i)
		{
			this.cubeList.get(i).render(buffer, par1);
		}

		GL11.glEndList();
		this.DDScompiled = true;
	}
}