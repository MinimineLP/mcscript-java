# Minecraft Script

[English README](README.md)

[Minecraft Script](https://github.com/MinimineLP/mcscript-java) ist eine Programmiersprache, welche für Mapmaker gemacht ist, welche professionelles Programmieren in Minecraft haben wollen. Diese Sprache hat einen eigenen [Discord Server](https://discordapp.com/invite/WvtCkyg), wenn ihr fragen, probleme, bugs findet, oder euch einfach nur mit anderen mapmakern verständigen wollt guckt doch mal vorbei.

## Orginal
Minecraft script is orginaly made by [@Stevertus](https://github.com/Stevertus), welcher [das Orginal](https://github.com/Stevertus/mcscript) in [NodeJS](https://nodejs.org/en/) programmiert hat.

## Installation (Nur für Windows)
##### Installations Assistent
1. Lade [dieses Archiv](https://github.com/MinimineLP/mcscript-java/raw/master/mcscript.zip) herunter, und entpacke ihn in einen Ordner deiner Wahl
2. Öffne cmd und schreibe "java". Wenn es funktioniert kannst du die nächsten paar Punkte hier überspringen. wenn der Befehl nicht gefunden wird tue folgendes
  - Öffne den Minecraft Launcher
  - Klicke auf "Profile"
  - Aktiviere "erweiterte Einstellungen"
  - Drücke auf ein Profil deiner Wahl
  - Aktiviere "java programm datei"
  - Kopiere den Pfad
  - Ersetze in der Datei mcscript.bat das Wort java durch den Pfad
  - Lösche das w aus "javaw" und speichere die datei (du kannst sie nun schließen)
Die Bat datei:
```bat
@echo off
if "%1"=="" (
echo Needing min one argument!
goto end
)
JAVA -jar "mcscript - minimal.jar" %*
:end
```
3. Öffne cmd in dem Ordner. Der Befehl "mcscript" funktioniert nun in dem Ordner, aber noch nicht in anderen Ordnern

4. Wenn du das ändern möchtest, öffne die datei globalify.bat mit einem einfachen doppelklick<br>
**WARNUNG: Dies kann mögicherweise andere Befehle überschreiben**

## Nutzung

Bitte nutze den [Guide]( https://github.com/Stevertus/mcscript/blob/master/README-DE.md#2-cli-commands) in Stevertus repository

## Autoren

* **Minimine** - *Entwicklung* - [Minimine](https://github.com/MinimineLP)
* **Stevertus** - *Orginale Arbeit (NodeJS)* - [Stevertus](https://github.com/Stevertus)

## Lizenz

Dieses Projekt ist lizensiert unter einer MIT Lizens. Weitere Informationen [hier](LICENSE)

## Fehler & Bugs
Es wäre super nett, wenn du, insofern di Fehler findest diese über die [Github Issues Funktion](https://github.com/MinimineLP/mcscript-java/issues) oder über discord meldest. [mcscript discord](https://discordapp.com/invite/WvtCkyg)
