#version 460 core

in vec2 pass_textureCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 color;
uniform int hasTexture;

void main() {
    if(hasTexture == 1) {
        fragColor = color * texture(textureSampler, pass_textureCoord);
    } else {
        fragColor = color;
    }
}