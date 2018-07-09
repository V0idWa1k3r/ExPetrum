package v0id.exp.client.mca;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utils {

    public static final Vector3f UNIT_X = new Vector3f(1, 0, 0);
    public static final Vector3f UNIT_Y = new Vector3f(0, 1, 0);
    public static final Vector3f UNIT_Z = new Vector3f(0, 0, 1);

	public static FloatBuffer makeFloatBuffer(float[] arr)
    {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	/**
	 * Make a direct NIO ByteBuffer from an array of floats
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	public static ByteBuffer makeByteBuffer(byte[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length);
		bb.order(ByteOrder.nativeOrder());
		bb.put(arr);
		bb.position(0);
		return bb;
	}

	/** Get the quaternion from a matrix. We need to transpose the matrix. */
	public static Quaternion getQuaternionFromMatrix(Matrix4f matrix)
	{
		Matrix4f copy = new Matrix4f(matrix);
		return fromMatrix((Matrix4f) copy.transpose());
	}

    public static Quaternion fromMatrix(Matrix4f mat)
    {
        double T = 1 + mat.m00 + mat.m11 + mat.m22;
        float x = 0, y = 0, z = 0, w = 0;
        if( T > 0.00000001 )
        {
            double S = Math.sqrt(T) * 2;
            x = (float) (( mat.m12 - mat.m21 ) / S);
            y = (float) (( mat.m02 - mat.m20 ) / S);
            z = (float) (( mat.m10 - mat.m01 ) / S);
            w = (float) (0.25 * S);
        }
        else if(T == 0)
        {
            if ( mat.m00 > mat.m11 && mat.m00 > mat.m22 )  {	// Column 0:
                double S  = Math.sqrt( 1.0 + mat.m00 - mat.m11 - mat.m22 ) * 2;
                x = (float) (0.25 * S);
                y = (float) ((mat.m10 + mat.m01 ) / S);
                z = (float) ((mat.m02 + mat.m20 ) / S);
                w = (float) ((mat.m21 - mat.m12 ) / S);
            } else if ( mat.m11 > mat.m22 ) {			// Column 1:
                double S  = Math.sqrt( 1.0 + mat.m11 - mat.m00 - mat.m22 ) * 2;
                x = (float) ((mat.m10 + mat.m01 ) / S);
                y = (float) (0.25 * S);
                z = (float) ((mat.m21 + mat.m12 ) / S);
                w = (float) ((mat.m02 - mat.m20 ) / S);
            } else {						// Column 2:
                double S  = Math.sqrt( 1.0 + mat.m22 - mat.m00 - mat.m11 ) * 2;
                x = (float) ((mat.m02 + mat.m20 ) / S);
                y = (float) ((mat.m21 + mat.m12 ) / S);
                z = (float) (0.25 * S);
                w = (float) ((mat.m10 - mat.m01 ) / S);
            }
        }

        return new Quaternion(x, y, z, w);
    }

	public static Quaternion getQuaternionFromEulers(float x, float y, float z) {
		Quaternion quatX = new Quaternion(1, 0, 0, (float) Math.toRadians(x));
		Quaternion quatY = new Quaternion(0, 1, 0, (float) Math.toRadians(y));
		Quaternion quatZ = new Quaternion(0, 0, 1, (float) Math.toRadians(z));
        Quaternion.mul(quatY, quatX, quatY);
        Quaternion.mul(quatZ, quatY, quatZ);
		return quatZ;
	}

    public static Matrix4f createFromQuaternion(Quaternion q1)
    {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = (1.0f - 2.0f*q1.y*q1.y - 2.0f*q1.z*q1.z);
        matrix4f.m10 = (2.0f*(q1.x*q1.y + q1.w*q1.z));
        matrix4f.m20 = (2.0f*(q1.x*q1.z - q1.w*q1.y));

        matrix4f.m01 = (2.0f*(q1.x*q1.y - q1.w*q1.z));
        matrix4f.m11 = (1.0f - 2.0f*q1.x*q1.x - 2.0f*q1.z*q1.z);
        matrix4f.m21 = (2.0f*(q1.y*q1.z + q1.w*q1.x));

        matrix4f.m02 = (2.0f*(q1.x*q1.z + q1.w*q1.y));
        matrix4f.m12 = (2.0f*(q1.y*q1.z - q1.w*q1.x));
        matrix4f.m22 = (1.0f - 2.0f*q1.x*q1.x - 2.0f*q1.y*q1.y);

        matrix4f.m03 = (float) 0.0;
        matrix4f.m13 = (float) 0.0;
        matrix4f.m23 = (float) 0.0;

        matrix4f.m30 = (float) 0.0;
        matrix4f.m31 = (float) 0.0;
        matrix4f.m32 = (float) 0.0;
        matrix4f.m33 = (float) 1.0;

        return matrix4f;
    }
}
