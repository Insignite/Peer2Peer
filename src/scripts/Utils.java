package scripts;

import java.io.File;

class Utils{
    public File checkDirectory(String fileDirectory){
        File sourceDirectory = new File(fileDirectory);
        if(!sourceDirectory.isDirectory()){
            sourceDirectory.mkdirs();
            System.out.println("New Folder creater at " + fileDirectory);
        }
        return sourceDirectory;

    }
}