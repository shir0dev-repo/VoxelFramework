#version 400 core

in vec2 pass_uv;

out vec4 out_colour;

uniform sampler2D textureSampler;

void main() {
    out_colour = texture(textureSampler, pass_uv) * vec4(1, 1, 1, 1);
}