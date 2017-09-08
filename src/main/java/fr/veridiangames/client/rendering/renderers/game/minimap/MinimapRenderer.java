package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.MinimapShader;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static fr.veridiangames.core.maths.Mathf.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MinimapRenderer
{
	private int width, height;
	private World world;

	private FloatBuffer positionsBuffer;
	private FloatBuffer colorsBuffer;
	private int vao, vbo, cbo;

	public MinimapRenderer(int width, int height)
	{
		this.world = Ubercube.getInstance().getGameCore().getGame().getWorld();
		this.width = width;
		this.height = height;
		createBuffer();
	}

	public void createBuffer()
	{
		positionsBuffer = BufferUtils.createFloatBuffer(world.getWorldSize() * Chunk.SIZE * world.getWorldSize() * Chunk.SIZE * 4 * 3);
		colorsBuffer = BufferUtils.createFloatBuffer(world.getWorldSize() * Chunk.SIZE * world.getWorldSize() * Chunk.SIZE * 4 * 4);

		for (int x = 0; x < world.getWorldSize() * Chunk.SIZE; x++)
		{
			for (int z = 0; z < world.getWorldSize() * Chunk.SIZE; z++)
			{
				int xx = x;
				int zz = z;
				int block = world.getHeighestBlockAt(xx, zz);

				Color4f c = new Color4f(block);

				positionsBuffer.put(x).put(z).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(x + 1).put(z).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(x + 1).put(z + 1).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(x).put(z + 1).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
			}
		}
		positionsBuffer.flip();
		colorsBuffer.flip();

		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		cbo = Buffers.createVertexBuffer();

		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0L);

		glBindVertexArray(0);

		positionsBuffer.clear();
		colorsBuffer.clear();
		positionsBuffer = null;
		colorsBuffer = null;
	}

	public void update()
	{
//		colorsBuffer = BufferUtils.createFloatBuffer((radius * 2) * (radius * 2) * 4 * 4);
//
//		Vec2 p = Ubercube.getInstance().getGameCore().getGame().getPlayer().getPosition().xz();
//		int x0 = (int) round(p.x - radius);
//		int z0 = (int) round(p.y - radius);
//
//		for (int x = 0; x < radius * 2; x++)
//		{
//			for (int z = 0; z < radius * 2; z++)
//			{
//				int xx = x + x0;
//				int zz = z + z0;
//				int block = world.getHeighestBlockAt(xx, zz);
//				Color4f c = new Color4f(block);
//				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
//				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
//				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
//				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
//			}
//		}
//		colorsBuffer.flip();
//
//		glBindBuffer(GL_ARRAY_BUFFER, cbo);
//		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
//
//		colorsBuffer.clear();
//		colorsBuffer = null;
	}

	public void render(MinimapShader shader, float scale)
	{
		Vec2 p = Ubercube.getInstance().getGameCore().getGame().getPlayer().getPosition().xz();
		Vec2 dir = Ubercube.getInstance().getGameCore().getGame().getPlayer().getRotation().getForward().xz().normalize();
		float yRot = toDegrees(atan2(dir.y, dir.x));
		System.out.println(yRot);
		shader.setModelViewMatrix(Mat4.translate(width / 2, height / 2, 0).mul(Mat4.rotate(0, 0, -yRot - 90).mul(Mat4.translate(-p.x * scale, -p.y * scale, 0).mul(Mat4.scale(scale, scale, scale)))));

		glBindVertexArray(vao);
		glDrawArrays(GL_QUADS, 0, world.getWorldSize() * Chunk.SIZE * world.getWorldSize() * Chunk.SIZE * 4);
		glBindVertexArray(0);
	}
}
