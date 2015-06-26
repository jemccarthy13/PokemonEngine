######################################################################
# Given a file "RAWMOVES.txt" that contains the copied table from the
# bulbapedia website this script will parse the \t delimited table
# and output a .MDAT formatted file.
######################################################################
def main():
    openFile = open("RAWMOVES.txt")
    outFile = open("MOVES.MDAT","w")
    for line in openFile:
        x = line.split("\t")[1:-1]
        y = x[:3]
        y.extend(x[4:])

        name = y[0].upper()
        if not name == "NAME":
            power = y[4].strip()
            power = 0 if power == "—" or power=="" else int(power)
            cat = y[2].strip().upper().replace("STATUS","STAT")
            t = y[1].strip().upper()
            pp = y[3].strip()
            pp = 0 if pp == "—" or pp == "" else pp
            accuracy = y[5].strip()
            accuracy = 100 if accuracy == "—" or accuracy =="" else int(accuracy.replace("%",""))
            
            print (name, power, cat, t, pp, accuracy)
            with open("MOVES.MDAT", "a") as outFile:
                outFile.write(name+","+str(power)+","+cat+","+t+","+pp+","+str(accuracy)+"\n")
                
main()
