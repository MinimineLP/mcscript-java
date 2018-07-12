# Minecraft Script

[Minecraft Script](https://github.com/MinimineLP/mcscript-java) is a programming-language made for mapmakers that want a more professional way for programming minecraft than just normal ingame commands. This Language has a [Discord Server](https://discordapp.com/invite/WvtCkyg), if you have questions, feature requests, found bugs, or just want to see what other cool people do with this Language join the  Discord Server

## Orginal
Minecraft Script is made by [@Stevertus](https://github.com/Stevertus), who wrote [the orginal](https://github.com/Stevertus/mcscript) compiler in [NodeJS](https://nodejs.org/en/).

## Installation (just for windows)
Installation guide
1. Download [this archive](https://github.com/MinimineLP/mcscript-java/raw/master/mcscript.zip) and extract it to a folder of your choice
2. Open cmd and type java, if it works you can skip the next few steps
  - open the Minecraft Launcher
  - click on "profiles"
  - enable "advanced settings"
  - click on a profile of your choice
  - enable "java programm file"
  - copy the path
  - in the file "mcscript.bat" replace the word "java" with the path
  - remove the w from the javaw in the path and save the file (you can now close it)
Batch file:
```bat
@echo off
if "%1"=="" (
echo Needing min one argument!
goto end
)
JAVA -jar "mcscript - minimal.jar" %*
:end
```
3. Open cmd in the folder, now the command mcscript works in this folder, but not in other folders

4. If you like to fix this, execute the globalify.bat file. then you just have to enter "y", and the command is global available (just on your user)<br>
**WARNING: This may overwrite other commands**

## Usage

Use the [Guide]( https://github.com/Stevertus/mcscript#cli-commands) in Stevertus repository

## Authors

* **Minimine** - *Initial work* - [Minimine](https://github.com/MinimineLP)
* **Stevertus** - *Orginal work (NodeJS)* - [Stevertus](https://github.com/Stevertus)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Issues
It would be very nice, if you find a issue to report it via the github issue function, or on the [mcscript discord](https://discordapp.com/invite/WvtCkyg)
