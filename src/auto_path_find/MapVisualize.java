import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/*
 * A brief document on the BMP format.
 * There are several variants of the BMP format. However, most programs implement the BITMAPINFOHEADER format.
 * In this format, the BMP file consists of a header of 54 bytes, and pixel data after the header.
 * The width of the image is a 4 bytes integer at offset 0x12, and the height at offset 0x16.
 * Each pixel is represented with 3 bytes (RGB). The pixel data is ordered from bottom to top, left to right.
 */

public class MapVisualize {
	static int cellHeight = 9;
	static int cellWidth = 9;
	
	static void writeInt(byte[] array, int offset, int data) {
		array[offset] = (byte) data;
		array[offset + 1] = (byte) (data >> 8);
		array[offset + 2] = (byte) (data >> 16);
		array[offset + 3] = (byte) (data >> 24);
	}
	
	static void writeShort(byte[] array, int offset, short data) {
		array[offset] = (byte) data;
		array[offset + 1] = (byte) (data >> 8);
	}
	
	static void writeRGB(byte[] pixeldata, int bitmapWidth, int row, int col, byte red, byte green, byte blue) {
		int height = pixeldata.length / (bitmapWidth * 3) - 1;
		row = height - row;
		pixeldata[(row * bitmapWidth + col) * 3 + 2] = red;
		pixeldata[(row * bitmapWidth + col) * 3 + 1] = green;
		pixeldata[(row * bitmapWidth + col) * 3] = blue;
	}
	
	static int readRGB(byte[] pixeldata, int bitmapWidth, int row, int col) {
		int height = pixeldata.length / (bitmapWidth * 3) - 1;
		row = height - row;
		int result;
		result = pixeldata[(row * bitmapWidth + col) * 3 + 2] & 0xff;
		result <<= 8;
		result += pixeldata[(row * bitmapWidth + col) * 3 + 1] & 0xff;
		result <<= 8;
		result += pixeldata[(row * bitmapWidth + col) * 3] & 0xff;
		return result;
	}
	
	static void initializeHeader(byte[] header, int bitmapHeight, int bitmapWidth) {
		header[0] = 'B';
		header[1] = 'M';
		writeInt(header, 2, 54 + bitmapHeight * bitmapWidth * 3);
		writeInt(header, 6, 0);
		writeInt(header, 10, 54);
		writeInt(header, 14, 40);
		writeInt(header, 18, bitmapWidth);
		writeInt(header, 22, bitmapHeight);
		writeShort(header, 26, (short)1);
		writeShort(header, 28, (short)24);
		writeInt(header, 30, 0);
		writeInt(header, 34, 0);
		writeInt(header, 38, 72);
		writeInt(header, 42, 72);
		writeInt(header, 46, 0);
		writeInt(header, 50, 0);
	}
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usage: MapVisualize [input map filename] [output bitmap filename] [solution file]");
			return;
		}
		try {
			String input = new String(Files.readAllBytes(Paths.get(args[0])));
			TerrainMap map = new TerrainMap();
			map.fromString(input);
			
			byte header[] = new byte[54];
			int bitmapHeight = map.height * cellHeight;
			int bitmapWidth = map.width * cellWidth;
			byte pixeldata[] = new byte[bitmapHeight * bitmapWidth * 3];
			initializeHeader(header, bitmapHeight, bitmapWidth);
			
			for(int i = 0;i < map.height;++i) {
				for(int j = 0;j < map.width;++j) {
					switch(map.data[i][j]) {
					case 0:
						for(int u = 0;u < cellHeight;++u) {
							for(int v = 0;v < cellWidth;++v) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + u, j * cellWidth + v, (byte)0, (byte)0, (byte)0);
							}
						}
						break;
					case 1:
					case 3:
						for(int u = 0;u < cellHeight;++u) {
							for(int v = 0;v < cellWidth;++v) {
								if(u == 0 || u == cellHeight - 1 || v == 0 || v == cellWidth - 1) {
									writeRGB(pixeldata, bitmapWidth, i * cellHeight + u, j * cellWidth + v, (byte)0, (byte)0, (byte)0);
								} else {
									writeRGB(pixeldata, bitmapWidth, i * cellHeight + u, j * cellWidth + v, (byte)255, (byte)255, (byte)255);
								}
							}
						}
						break;
					case 2:
					case 4:
						for(int u = 0;u < cellHeight;++u) {
							for(int v = 0;v < cellWidth;++v) {
								if(u == 0 || u == cellHeight - 1 || v == 0 || v == cellWidth - 1) {
									writeRGB(pixeldata, bitmapWidth, i * cellHeight + u, j * cellWidth + v, (byte)0, (byte)0, (byte)0);
								} else {
									writeRGB(pixeldata, bitmapWidth, i * cellHeight + u, j * cellWidth + v, (byte)127, (byte)127, (byte)127);
								}
							}
						}
						break;
					}
					
					if(map.data[i][j] >= 3) {
						for(int p = -1;p <= 1;++p) {
							for(int q = -1;q <= 1;++q) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + cellHeight / 2 + p, j * cellWidth + cellWidth / 2 + q, (byte)0, (byte)0, (byte)255);
							}
						}
						if(i > 0 && map.data[i - 1][j] >= 3) {
							for(int k = 0;k < cellHeight / 2;++k) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + k, j * cellWidth + cellWidth / 2, (byte)0, (byte)0, (byte)255);
							}
						}
						if(i < map.height - 1 && map.data[i + 1][j] >= 3) {
							for(int k = cellHeight / 2 + 1;k < cellHeight;++k) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + k, j * cellWidth + cellWidth / 2, (byte)0, (byte)0, (byte)255);
							}
						}
						if(j > 0 && map.data[i][j - 1] >= 3) {
							for(int k = 0;k < cellWidth / 2;++k) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + cellHeight / 2, j * cellWidth + k, (byte)0, (byte)0, (byte)255);
							}
						}
						if(j < map.width - 1 && map.data[i][j + 1] >= 3) {
							for(int k = cellWidth / 2 + 1;k < cellWidth;++k) {
								writeRGB(pixeldata, bitmapWidth, i * cellHeight + cellHeight / 2, j * cellWidth + k, (byte)0, (byte)0, (byte)255);
							}
						}
					}
				}
			}
			
			for(int u = 1;u < cellHeight - 1;++u) {
				for(int v = 1;v < cellWidth - 1;++v) {
					int color = readRGB(pixeldata, bitmapWidth, map.startX * cellHeight + u, map.startY * cellWidth + v);
					if(color != 255) {
						writeRGB(pixeldata, bitmapWidth, map.startX * cellHeight + u, map.startY * cellWidth + v, (byte)255, (byte)0, (byte)0);
					}
				}
			}
			for(int u = 1;u < cellHeight - 1;++u) {
				for(int v = 1;v < cellWidth - 1;++v) {
					int color = readRGB(pixeldata, bitmapWidth, map.goalX * cellHeight + u, map.goalY * cellWidth + v);
					if(color != 255) {
						writeRGB(pixeldata, bitmapWidth, map.goalX * cellHeight + u, map.goalY * cellWidth + v, (byte)0, (byte)255, (byte)0);
					}
				}
			}
			
			if(args.length >= 3) {
				Scanner scan = new Scanner(new File(args[2]));
				while(scan.hasNextInt()) {
					int cellX = scan.nextInt();
					int cellY = scan.nextInt();
					for(int u = 2;u < cellHeight - 2;++u) {
						for(int v = 2;v < cellWidth - 2;++v) {
							int color = readRGB(pixeldata, bitmapWidth, cellX * cellHeight + u, cellY * cellWidth + v);
							if(color != 255) {
								writeRGB(pixeldata, bitmapWidth, cellX * cellHeight + u, cellY * cellWidth + v, (byte)255, (byte)0, (byte)127);
							}
						}
					}
				}
				scan.close();
			}
			
			FileOutputStream writer = new FileOutputStream(args[1]);
			writer.write(header);
			writer.write(pixeldata);
			writer.close();
		} catch(IOException e) {
			System.out.println("IOException occurred");
			e.printStackTrace();
		}
	}
}
