<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
			<title>Survey Import</title>
			<meta http-equiv="cache-control" content="no-store, no-cache, must-revalidate" />
			<meta http-equiv="Pragma" content="no-store, no-cache" />
			<meta http-equiv="Expires" content="0" />
			<meta name="google" value="notranslate" />         
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />		
			<script type="text/javascript" src="script/jquery-1.6.2.min.js"></script>
			<script type="text/javascript" src="script/sessionping.js"></script>
		</head>
    <body>
       <h1>Select a logo image to import and specify a position:</h1>
       <form method="post" action="uploadLogo.htm" enctype="multipart/form-data">
           <input type="file" name="fileData" />
           <label>Position:</label>
           <select name="position">
           	<option value="1">Top right corner</option>
           </select>
           <input type="submit" />
       </form>
    </body>
</html>