#version 460 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 vertexNormal;

uniform mat4 modelLightViewMatrix;
uniform mat4 orthoProjectionMatrix;

void main() {
    gl_Position = orthoProjectionMatrix * modelLightViewMatrix * vec4(position, 1.0);
}