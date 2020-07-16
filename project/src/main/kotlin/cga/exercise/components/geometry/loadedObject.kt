package cga.exercise.components.geometry

import cga.exercise.components.texture.Texture2D
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

class loadedObject(objectPath : String, diffTexturPath: String, emitTexturPath: String, specTexturPath: String) {

    var meshData : MutableList<OBJLoader.OBJMesh>
    var createdMeshes : MutableList<Mesh>
    val diffTextur : Texture2D
    val emitTextur : Texture2D
    val specTextur : Texture2D
    val objectMaterial : Material
    var renderableObject : Renderable

    init {
        // Laden der Objektdaten & speichern der Meshdaten
        val loadedData = OBJLoader.loadOBJ(objectPath)
        meshData = loadedData.objects[0].meshes
        // Erstellen der Texturen
        diffTextur = Texture2D(diffTexturPath, true)
        diffTextur.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)
        emitTextur = Texture2D(emitTexturPath, true)
        emitTextur.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)
        specTextur = Texture2D(specTexturPath, true)
        specTextur.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)
        // Erstellen des Materials mit den erstellten Texturen
        objectMaterial = Material(diffTextur, emitTextur, specTextur, 60f, Vector2f(64.0f, 64.0f))
        // Erstellen der Meshes
        createdMeshes = mutableListOf()
        for ((index, aktMeshData) in meshData.withIndex()) {
            createdMeshes.add(
                    Mesh(
                            aktMeshData.vertexData,
                            aktMeshData.indexData,
                            arrayOf(
                                    VertexAttribute(3, GL11.GL_FLOAT, 32, 0),
                                    VertexAttribute(2, GL11.GL_FLOAT, 32, 12),
                                    VertexAttribute(3, GL11.GL_FLOAT, 32, 20)
                            ),
                            objectMaterial
                    )
            )
        }
        // Aus den erstellten Daten ein Renderable Object erstellen
        renderableObject = Renderable(createdMeshes)
    }
}