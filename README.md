# JCord API   
JCord API is a set of tools to make a development of JCord easier, it is not really recommended to use it without JCord, but it is certainly possible.  

## Installation  
API is automatically installed with JCord, but you can also install it separately.  
There are multiple ways to install it:  
1) Building it from source
   1) Clone the repository
   2) Run `gradlew build`
2) Downloading prebuilt source from [releases](https://github.com/KoblizekXD/JCord-api)
3) JitPack:
   1) Add JitPack repository to your build file:
   ```groovy
   repositories {
     ...
     maven { url 'https://jitpack.io' }
   }
   ```
   2) Add the dependency:
   ```groovy
    dependencies {
      implementation 'com.github.KoblizekXD:JCord-api:VERSOION'
    }
    ```

## Contributing  
Any contributions are welcome, just fork the repo and make a pull request.  
This project should be kept mostly unmaintained, as it is just a set of tools for JCord.  

## License
JCord API is licensed under the [MIT License](./LICENSE)
