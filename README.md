# KFileBuilder
A simple desktop application to build directories and files from templates. Built with [Compose for Desktop](https://github.com/JetBrains/compose-multiplatform) using Kotlin.

## Features
- View directories in the computer and select one of them.
- Pre-visualize files in tree before saving the template.
- Create an infinite amount of templates, edit and delete them.
- Template generation with parameters support.

## Parameters support 
You can add parameters in the new files of the template, as follows:

#### Manage template with params
![image](https://github.com/Quentixx/KFileBuilder/assets/11049729/2280f7f5-2599-40e3-a3dc-caa7bdac2741)


By clicking on the button to generate the structure of the template, and if you have mentioned some parameters, a pop-up window will ask you for the replacements you want at this moment, complete and confirm the creation:

#### Provide replacements of params
![image](https://github.com/Quentixx/KFileBuilder/assets/11049729/f69dbba6-863c-4115-9913-a1bc4f012a58) ![image](https://github.com/Quentixx/KFileBuilder/assets/11049729/f616a68e-946b-4949-84ab-11d7e6a613ff)


When you confirm that you want to create the template, the files are created on the computer, and Computer Explorer opens the source directory where the template was created.

Invalid characters ('/', '\\', '\"', ':', '?', '<', '>', '*') provided are transformed into a hyphen ('-') to ensure ease of use.

#### Generation rendering
![image](https://github.com/Quentixx/KFileBuilder/assets/11049729/8daf23ce-f726-43c2-bb24-dd3c2a43c34f)


## TODO
- Delete a file in the template management view when user clicks on the bin icon.
- File order In the tree selector, the folders must be on top of the other files.
- Translation system with languages supports (EN, FR, ES).
- Application auto update on launching if new version are available on this github project.
