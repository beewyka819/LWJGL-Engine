#version 460 core

in vec2 pass_textureCoord;
in vec3 mvPos;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 ambientLight;

void main() {
    fragColor = vec4(ambientLight, 1.0) * texture(textureSampler, pass_textureCoord);
}