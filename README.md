# LWJGL-Engine
A rudimentary 3D graphical engine utilizing OpenGL in Java. The engine supports loading and
rendering of 3D models of varying formats, with additional support for diffuse and specular lighting. It also supports
shadow mapping for shadows.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing 
purposes.
### Prerequisites
Must have the following installed

- Apache Maven
  - See how to install Maven [here](https://maven.apache.org/install.html)
- JDK 1.8
  - See how to install JDK 1.8 [here](https://www.foxinfotech.in/2019/03/how-to-install-jdk-8-on-windows-10.html)

## Installing
First, download one of the branches, either through git or by manually downloading the
project on github and extracting the contents to a location on your local machine.

To clone the repository via git, make sure you're in an unmanaged directory and enter the following command:
```
git clone https://github.com/beewyka819/LWJGL-Engine/ [directory name]
```
You can then checkout the branch of your choosing
```
git checkout [branch name]
```

Once the project is on your machine, navigate to the directory with pom.xml and compile it
```
mvn package
```
In the target folder, there should now be multiple jars. The one you want does not have the 'original' prefix
nor the 'shaded' suffix.

## Built With
- [Maven](https://maven.apache.org/)
- [LWJGL](https://www.lwjgl.org/)

## Authors
- **Patrick Sullivan** - [beewyka819](https://github.com/beewyka819)

## License
This project is licensed under the [Apache v2.0 License](https://www.apache.org/licenses/LICENSE-2.0) - see the [LICENSE.md](LICENSE.md) file for details
## Acknowledgements
-**Antonio Hernández Bejarano** - [3D Game Development with LWJGL 3](https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/)
