<?php
$directory = "FileSync";
$results_array = array();
if (is_dir($directory))
{
    if ($handle = opendir($directory))
    {
        while(($file = readdir($handle)) !== FALSE)
        {
	    if (file_exists($filename = "FileSync/".$file)&&$file!="."&&$file!="..") {
    		echo "$file " .filemtime($filename)."\n";
		}

        }
        closedir($handle);
    }
}



?>