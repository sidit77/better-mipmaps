# Better Mipmaps

## Overview

![beforeafter](https://github.com/sidit77/better-mipmaps/assets/5053369/107f30d6-7523-4537-b268-c4cf7dcc0d3f)


Have you ever noticed how noisy Minecraft looks in the distance when using a high-resolution resource pack?
The reason is that by default, Minecraft only supports full [mipmaps](https://en.wikipedia.org/wiki/Mipmap) for 16x16 textures and below. Anything higher than that will have incomplete mipmaps.
This mod fixes this by increasing the maximum level of mipmaps to 10 (1024x1024).

The optimal mipmap level depends on the resolution of your resource pack and can be found in the table below:
| Texture Resolution | Optimal Mipmap Level |
|--------------------|----------------------|
|        1           |          0           |
|        2           |          1           |
|        4           |          2           |
|        8           |          3           |
|       16           |          4           |
|       32           |          5           |
|       64           |          6           |
|      128           |          7           |
|      256           |          8           |
|      512           |          9           |

## Technical Details

The problem with generating a full mipmap pyramids for Minecraft textures is that Minecraft combines all block textures into one big texture atlas. The maximum mipmap level of this atlas is a smallest maximum mipmap level of any individual texture.
So even when using a 99% complete resource pack, a single left-over 16x16 texture will limit the atlas to 4 mipmap levels. To work around this, this mod will automatically attempt to upscale smaller textures before the stitching process.
