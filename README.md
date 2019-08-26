##### Mars Rover Mission
## Intro
* Download project from [GitHub::mars-rover](http://github.com/inibarker/mars-rover)
* Run build `mvn clean package` on project folder
* Execute command line mode `java -jar target/mars-rover-x.jar text`
### Requirement
As part of the team that explores Mars by sending remotely controlled vehicles to the surface of the planet.
Must develop an API that translates the commands sent from earth to instructions that are understood by the rover.
- Given the initial starting point (x,y) of a rover and the direction (N,S,E,W) it is facing.
- The rover receives a character array of commands.
- Implement commands that move the rover forward/backward (f,b).
- Implement commands that turn the rover left/right (l,r).
- Implement wrapping from one edge of the grid to another. (planets are spheres after all)
- Implement obstacle detection before each move to a new square. If a given sequence of commands encounters an obstacle, the rover moves up to the last possible point, aborts the sequence and reports the obstacle.
### Features
- While app boots loads configurable data about map and vehicle (Mars and Rover).
- Pay attention to the on-screen instructions in order to input correct data.
- While navigating with the rover you can quit the text console with `QUIT` command.
- You can always close the program with the Control-C command.
#
## Notes
Inspired by [Kata Log Rocks](http://kata-log.rocks/mars-rover-kata) 
###### by Ini Barker



