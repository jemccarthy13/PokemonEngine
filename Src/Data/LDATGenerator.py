import os
######################################################################
# Given a file "RAWLOCS.txt" that contains the copied table from the
# bulbapedia website this script will parse the \t delimited table
# and output a series of .LDAT formatted files in a Locations folder
# in the current working directory
######################################################################
def main():
    with open("RAWLOCS.txt", "r") as openFile:
        for line in openFile:
            x = line.split("\t")
            x = [y.strip() for y in x]
            # name is actual name
            name = x[1]
            # filename is fil
            fileName = name.replace(" ", "").replace(".","_").replace("'","").upper()
            fileName +=".LDAT"
            print (fileName)
            with open(os.path.join("Locations",fileName),"w") as outFile:
                outFile.write(name)

main()
