#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

out vec4 fragColor;
uniform samplerCube map;
uniform sampler2D shadowMap;
uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;

in vec3 v_color;
in vec3 v_normal;
in vec3 worldPosition;
in vec4 lightPosition;

float calcShadowFactor(vec4 lightPosition)
{
    vec3 projCoords = lightPosition.xyz / lightPosition.w;
    projCoords = projCoords * 0.5 + 0.5;
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    float currentDepth = projCoords.z;

    if (currentDepth - 0.00001 > closestDepth)
    	return 0.5;
    return 1.0;
}

void main(void)
{
	float dist = distance(cameraPosition, worldPosition) / fogDistance * 2 - 0.8;
	if (dist > 1)
		dist = 1;
	if (dist < 0)
		dist = 0;

	vec4 color = vec4(v_color, 1.0);
	if (in_color != vec4(-1, -1, -1, -1))
		color = in_color;

	vec3 eyeDirection = normalize(cameraPosition - worldPosition);
	vec3 reflectDirection = reflect(-eyeDirection, v_normal);
	vec4 reflectionColor = textureCube(map, reflectDirection);
	float shadowFactor = calcShadowFactor(lightPosition);
	vec4 finalColor = mix(color, reflectionColor, 0) * 1.2f * vec4(shadowFactor, shadowFactor, shadowFactor, 1.0);
	fragColor = mix(finalColor, FOG_COLOR, dist);
}
