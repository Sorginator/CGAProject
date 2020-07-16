package cga.exercise.components.geometry

import cga.exercise.components.texture.Texture2D
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.joml.Vector4i
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

class loadedObject(
        objectPath : String, diffTexturPath: String, emitTexturPath: String, specTexturPath: String,
        texturMipmap : Boolean = true,
        diffTexParams : Vector4i = Vector4i(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR),
        emitTexParams : Vector4i = Vector4i(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR),
        specTexParams : Vector4i = Vector4i(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR),
        materialShininess : Float = 60f, materialTcMultiplier : Vector2f = Vector2f(64.0f, 64.0f)) {

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
        diffTextur = Texture2D(diffTexturPath, texturMipmap)
        diffTextur.setTexParams(diffTexParams[0], diffTexParams[2], diffTexParams[3], diffTexParams[4])
        emitTextur = Texture2D(emitTexturPath, texturMipmap)
        emitTextur.setTexParams(emitTexParams[0], emitTexParams[2], emitTexParams[3], emitTexParams[4])
        specTextur = Texture2D(specTexturPath, texturMipmap)
        specTextur.setTexParams(specTexParams[0], specTexParams[2], specTexParams[3], specTexParams[4])
        // Erstellen des Materials mit den erstellten Texturen
        objectMaterial = Material(diffTextur, emitTextur, specTextur, materialShininess, materialTcMultiplier)
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