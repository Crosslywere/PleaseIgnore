#version 330 core
layout (location = 0) out vec4 oColorAttachment0;
in vec3 iNorm;
in vec2 iTexCoord;
uniform vec3 uViewPos;
uniform vec3 uLightPos;
uniform float uLevels;
uniform sampler2D uTexture;
float posterizeAmount(float grayscale) {
    float lower     = floor(grayscale * uLevels) / uLevels;
    float lowerDiff = abs(grayscale - lower);
    float upper     = ceil(grayscale * uLevels) / uLevels;
    float upperDiff = abs(grayscale - upper);
    float level     = lowerDiff <= upperDiff ? lower : upper;
    float adjustment= level / grayscale;
    return max(adjustment, 1./uLevels);
}
void main() {
    vec3 color = texture2D(uTexture, iTexCoord).rgb;
    vec3 lightDir = normalize(uLightPos - iNorm);
    float ambient = 0.5;
    float diffuse = max(dot(iNorm, lightDir), 0.0);
    color *= ambient + diffuse;
    color *= posterizeAmount(max(color.r, max(color.g, color.b)));
    oColorAttachment0 = vec4(color, 1.0);
}