#version 400 core

in vec3 position;
in vec2 uv;

out vec2 pass_uv;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position.xyz, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_uv = uv;
}