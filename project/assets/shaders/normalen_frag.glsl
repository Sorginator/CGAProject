#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 normal;
    vec2 texKoords;
    vec3 toCamera;
    vec3 toPointLight;
    vec3 toSpotLight;
} vertexData;

//uniforms!
//material
uniform sampler2D emit_tex;
uniform sampler2D diff_tex;
uniform sampler2D spec_tex;
uniform float shininess;

uniform vec3 colo;

//fragment shader output
out vec4 color;

void main(){
    //Normalisierter Scheißdreck
    vec3 N = normalize(vertexData.normal);
    vec3 V = normalize(vertexData.toCamera);
    color = vec4(N, 1.0f);
}

