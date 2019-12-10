#version 460 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 vertexNormal;

out vec2 pass_textureCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out vec4 mlightviewVertexPos;
out mat4 outModelViewMatrix;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 modelLightViewMatrix;
uniform mat4 orthoProjectionMatrix;

void main() {
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    pass_textureCoord = textureCoord;
    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
    mlightviewVertexPos = orthoProjectionMatrix * modelLightViewMatrix * vec4(position, 1.0);
    outModelViewMatrix = modelViewMatrix;
}