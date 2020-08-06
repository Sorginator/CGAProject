#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 normale;

//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

out struct VertexData
{
    vec3 normal;
} vertexData;


void main(){
    mat4 model_view = view_matrix * model_matrix;
    vec4 pos = model_view * vec4(position, 1.0f);

    gl_Position = projection_matrix * pos;
    vertexData.normal = (inverse(transpose(model_view)) * vec4(normale, 0.0)).xyz;
}
