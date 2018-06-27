package v0id.core.client.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import org.apache.commons.io.IOUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.VCLoggers;
import v0id.core.logging.LogLevel;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WavefrontObject
{
    public List<Vertex> getVertices()
    {
        return vertices;
    }

    private final List<Vertex> vertices = Lists.newArrayList();
	
	public WavefrontObject()
	{
		
	}
	
	public void load(InputStream is)
	{
		try
		{
			this.load(IOUtils.toString(is, StandardCharsets.UTF_8));
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Exception caught trying to read a model from an input stream!", ex);
		}
		finally
		{
			// Stream has been read to the very end, no reason to leave it open and potentially cause a leak
			IOUtils.closeQuietly(is);
		}
	}
	
	public void load(String s)
	{
		List<Vector3f> tempVertexes = Lists.newArrayList();
        List<Vector2f> tempUVs = Lists.newArrayList();
        List<Vector3f> tempNormals = Lists.newArrayList();
        List<Vector3f> tempFaces = Lists.newArrayList();
        Splitter splitter = Splitter.on(Pattern.compile("\r?\n")).omitEmptyStrings().trimResults();
        int lines = 0;
        for (String line : splitter.split(s))
        {
        	++lines;
        	// Ignore comments
        	if (line.charAt(0) == '#')
            {
                continue;
            }
        	
        	// Ignore user objects - this implementation only supports 1 object in a model
        	if (line.startsWith("o "))
            {
        		continue;
            }
        	
        	// Vertex
            if (line.startsWith("v "))
            {
                try
                {
                    String[] data = line.substring(2).split(" ");
                    tempVertexes.add(new Vector3f(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2])));
                }
                catch (Exception ex)
                {
                    VCLoggers.loggerErrors.log(LogLevel.Error, "Model has invalid vertex definition at line %d! Vertices are specified in euclidean space and must contain 3 numbers separated by whitespace!", ex, lines);
                }
            }
            
            // Vertex normal
            if (line.startsWith("vn "))
            {
                try
                {
                    String[] data = line.substring(3).split(" ");
                    tempNormals.add(new Vector3f(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2])));
                }
                catch (Exception ex)
                {
                	VCLoggers.loggerErrors.log(LogLevel.Error, "Model has invalid normal definition at line %d! Vertex normals are euclidean vectors and must contain 3 numbers separated by whitespaces!", ex, lines);
                }
            }

            // UV
            if (line.startsWith("vt "))
            {
                try
                {
                    String[] data = line.substring(3).split(" ");
                    tempUVs.add(new Vector2f(Float.parseFloat(data[0]), Float.parseFloat(data[1])));
                }
                catch (Exception ex)
                {
                	VCLoggers.loggerErrors.log(LogLevel.Error, "Model has invalid texture coordinates definition at line %d! Texture coordinates are bi-dimensional space vectors and must contain 2 numbers separated by whitespaces!", ex, lines);
                }
            }
            
            // Face
            if (line.startsWith("f "))
            {
                try
                {
                    String[] data = line.substring(2).split(" ");
                    for (String faceData : data)
                    {
                        String[] faceVertexData = faceData.split("/");
                        tempFaces.add(new Vector3f(Integer.parseInt(faceVertexData[0]), Integer.parseInt(faceVertexData[1]), Integer.parseInt(faceVertexData[2])));
                    }
                }
                catch (Exception ex)
                {
                	VCLoggers.loggerErrors.log(LogLevel.Error, "Model has invalid face definition at line %d! Faces are pointers to vertexes, uvs and normals. They must contain 3 sequence of 3 numbers separated by / separated by whitespaces(ex.: 3/3/3 1/3/3 5/5/9)! Before exporting your model make sure to split all it's faces into triangles!", ex, lines);
                }
            }
        }
        
        this.bakeGeometry(tempFaces, tempVertexes, tempUVs, tempNormals);
        tempVertexes.clear();
        tempUVs.clear();
        tempNormals.clear();
        tempFaces.clear();
        VCLoggers.loggerMod.log(LogLevel.Fine, "Wavefront object model loaded!");
	}
	
	public void bakeGeometry(List<Vector3f> faces, List<Vector3f> vertexData, List<Vector2f> uvData, List<Vector3f> normalData)
	{
		for (Vector3f faceData : faces)
		{
			int vertexIndex = (int) (faceData.getX() - 1);
			int uvIndex = (int) (faceData.getY() - 1);
			int normalIndex = (int) (faceData.getZ() - 1);
			this.vertices.add(new Vertex(vertexData.get(vertexIndex), uvData.get(uvIndex), normalData.get(normalIndex)));
		}
	}
	
	public void putVertices(BufferBuilder buffer)
	{
		for (Vertex v : this.vertices)
		{
			v.putVertex(buffer);
		}
	}
	
	public class Vertex
	{
		public final Vector3f position;
		public final Vector2f uvset;
		public final Vector3f normals;
		
		public Vertex(Vector3f v, Vector2f v1, Vector3f v2)
		{
			this.position = v;
			this.uvset = v1;
			this.normals = v2;
		}
		
		public void putVertex(BufferBuilder buffer)
		{
			// For some reason (probably because of MC texture loading?) textures end up flipped on the V axis. This fixes it by flipping the UVs again.
			buffer.pos(this.position.x, this.position.y, this.position.z).tex(this.uvset.x, 1 - this.uvset.y).normal(this.normals.x, this.normals.y, this.normals.z).endVertex();
		}
	}
}
