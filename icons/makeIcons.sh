#!/bin/sh
base=$1

if [ -z $base ]
  then
    echo No argument given
else
  ##
  ## iOS files
  #convert "$base" -resize 29x29!     "Icon-Small.png"
  #convert "$base" -resize 40x40!     "Icon-Small-40.png"
  #convert "$base" -resize 50x50!     "Icon-Small-50.png"
  #convert "$base" -resize 57x57!     "Icon.png"
  #convert "$base" -resize 58x58!     "Icon-Small@2x.png"
  #convert "$base" -resize 87x87!     "Icon-Small@3x.png"
  #convert "$base" -resize 60x60!     "Icon-60.png"
  #convert "$base" -resize 72x72!     "Icon-72.png"
  #convert "$base" -resize 76x76!     "Icon-76.png"
  #convert "$base" -resize 80x80!     "Icon-Small-40@2x.png"
  #convert "$base" -resize 100x100!   "Icon-Small-50@2x.png"
  #convert "$base" -resize 114x114!   "Icon@2x.png"
  #convert "$base" -resize 120x120!   "Icon-60@2x.png"
  #convert "$base" -resize 144x144!   "Icon-72@2x.png"
  #convert "$base" -resize 152x152!   "Icon-76@2x.png"
  #convert "$base" -resize 83.5x83.5! "Icon-83.5@2x.png"
  #convert "$base" -resize 180x180!   "Icon-60@3x.png"
  #convert "$base" -resize 512x512!   "iTunesArtwork"
  #convert "$base" -resize 1024x1024! "iTunesArtwork@2x"
  ##
  ## Android files
  convert "$base" -resize 36x36!    "Icon-ldpi.png"
  convert "$base" -resize 48x48!    "Icon-mdpi.png"
  convert "$base" -resize 72x72!    "Icon-hdpi.png"
  convert "$base" -resize 96x96!    "Icon-xhdpi.png"
  convert "$base" -resize 144x144!  "Icon-xxhdpi.png"
  convert "$base" -resize 192x192!  "Icon-xxxhdpi.png"
fi

