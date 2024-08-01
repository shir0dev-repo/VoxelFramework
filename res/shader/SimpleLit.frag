#version 400 core

in vec2 pass_uv;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float specular;
uniform float damper;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDotL = dot(unitNormal, unitLightVector);
    float brightness = max(nDotL, 0.3);
    vec3 diffuse = brightness * lightColour;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specular = dot(reflectedLightDirection, unitVectorToCamera);
    specular = max(specular, 0.0);
    float dampedSpec = pow(specular, specular);
    vec3 finalSpecular = dampedSpec * damper * lightColour;

    out_colour = vec4(diffuse, 1.0) * texture(textureSampler, pass_uv) + vec4(finalSpecular, 1.0);
}