Command to create HLS need to change the `scorpions.mp4` to your mp4 file
```
ffmpeg -i scorpions.mp4 -filter_complex '
[0:v]split=3[v1080][v720][v480];
[v1080]scale=-2:1080[v1080out];
[v720]scale=-2:720[v720out];
[v480]scale=-2:480[v480out]
' \
-map \[v1080out\] -c:v:0 libx264 -b:v:0 5000k -preset veryfast -g 48 -sc_threshold 0 \
-map \[v720out\]  -c:v:1 libx264 -b:v:1 3000k -preset veryfast -g 48 -sc_threshold 0 \
-map \[v480out\]  -c:v:2 libx264 -b:v:2 1000k -preset veryfast -g 48 -sc_threshold 0 \
-map a:0 -c:a:0 aac -b:a:0 128k \
-map a:0 -c:a:1 aac -b:a:1 128k \
-map a:0 -c:a:2 aac -b:a:2 128k \
-f hls \
-hls_time 6 \
-hls_playlist_type vod \
-hls_segment_filename "v%v/seg_%03d.ts" \
-master_pl_name master.m3u8 \
-var_stream_map "v:0,a:0 v:1,a:1 v:2,a:2" \
v%v/output.m3u8
```
