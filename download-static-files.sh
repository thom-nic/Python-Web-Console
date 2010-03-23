
# Download YUI files - they are hosted from YUI's servers when in production 
# but hosted locally when in debug (development mode)
 
yui_version=2.8.0r4
dest_base=src/main/webapp/static/hosted/yui/ 
yui_url=http://yui.yahooapis.com/${yui_version}/build
mkdir -p $dest_base
wget -P $dest_base $yui_url/base/base-min.css \
 $yui_url/reset-fonts-grids/reset-fonts-grids.css

file="yui_$yui_version.zip"

wget "http://yuilibrary.com/downloads/yui2/$file"
unzip $file "yui/build/*"
libs=(assets yahoo-dom-event selector json element connection container button)
for lib in "${libs[@]}" ; do
	mv yui/build/$lib $dest_base
done
rm -r $file yui/

# Download SyntaxHighlighter files for use in development:
dest_base=src/main/webapp/static/hosted/syntax/
mkdir $dest_base
sh_url=http://alexgorbatchev.com/pub/sh/current
wget -P $dest_base \
 $sh_url/styles/help.png $sh_url/styles/magnifier.png $sh_url/styles/printer.png \
 $sh_url/styles/page_white_code.png $sh_url/styles/page_white_copy.png \
 $sh_url/styles/shCore.css $sh_url/styles/shThemeFadeToGrey.css \
 $sh_url/scripts/shCore.js $sh_url/scripts/shBrushPython.js

# Ojay files must be included since there is no hosted version.  We use the 
# minified versions when in production (i.e. debug is off)
ojay_version=0.4.1
file=ojay-$ojay_version.zip
dest_base=src/main/webapp/static/js/ojay/

mkdir $dest_base
wget http://ojay.googlecode.com/files/$file
unzip $file "ojay-$ojay_version/ojay/*"
mv ojay-$ojay_version/ojay/* $dest_base
rm -r $file "ojay-$ojay_version/"
