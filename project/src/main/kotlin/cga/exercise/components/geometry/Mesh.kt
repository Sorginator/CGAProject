package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, private  val material: Material?) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {
        // Generate ID's
        vao = GL30.glGenVertexArrays()
        vbo = GL30.glGenBuffers()
        ibo = GL30.glGenBuffers()

        // Bind the Objects
        GL30.glBindVertexArray(vao)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ibo)

        // Upload mesh data
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexdata, GL30.GL_STATIC_DRAW)
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL30.GL_STATIC_DRAW)

        // Layout
        for ((index, item) in attributes.withIndex()) {
            GL30.glEnableVertexAttribArray(index)
            GL30.glVertexAttribPointer(
                    index,
                    item.n,
                    item.type,
                    false,
                    item.stride,
                    item.offset.toLong()
            )
        }

        // Anzahl Punkte zählen
        indexcount = indexdata.count()

        // Alles lösen
        GL30.glBindVertexArray(0)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    // Render the mesh
    fun render() {
        // Call the rendering method every frame
        GL30.glBindVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, indexcount, GL30.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    // Render mit Shaderübergabe
    fun render(shader: ShaderProgram)
    {
        material?.bind(shader)
        render()
    }

    // Deletes the previously allocated OpenGL objects for this mesh
    fun cleanup() {
        if (ibo != 0) GL15.glDeleteBuffers(ibo)
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }
}