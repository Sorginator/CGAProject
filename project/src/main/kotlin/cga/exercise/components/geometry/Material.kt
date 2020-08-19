package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var specular: Texture2D,
               var shininess: Float = 50.0f,
               var tcMultiplier : Vector2f = Vector2f(1.0f)){

    fun bind(shaderProgram: ShaderProgram) {
        diff.bind(0)
        shaderProgram.setUniform("diff_tex", 0)
        emit.bind(1)
        shaderProgram.setUniform("emit_tex", 1)
        specular.bind(2)
        shaderProgram.setUniform("spec_tex", 2)
        shaderProgram.setUniform("tcMultiplier", tcMultiplier)
        shaderProgram.setUniform("shininess", shininess)
    }
}