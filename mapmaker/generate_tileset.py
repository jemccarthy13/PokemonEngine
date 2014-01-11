def main():

    max_tiles = 91
    
    openFile = open("Tiles.tileset", "w")
    openFile.write("# Tileset generated by Python3.3\n")
    for x in range(0,max_tiles):
        string = str(x) + ", " + r"Tiles\tile-"+str(x)+".png, " + str(x)+", NormalTile\n"
        openFile.write(string)
    openFile.close()

    openFile = open("Tiles.mapeditor", "w")
    openFile.write("# mapeditor tileset generated by Python3.3\n")
    for x in range(0, max_tiles):
        string = str(x)+", " + r"Tiles\tile-"+str(x)+".png, New Tile " + str(x)+", No Type\n"
        openFile.write(string)
    openFile.close()
    
main()
        
