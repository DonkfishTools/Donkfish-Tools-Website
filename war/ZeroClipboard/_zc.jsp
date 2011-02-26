<script type="text/javascript" src="/ZeroClipboard/ZeroClipboard.js"></script>

<style type="text/css">
    .my_clip_button { width:150px; text-align:center; border:1px solid black; background-color:#ccc; margin:10px; padding:10px; cursor:default; font-size:9pt; } .my_clip_button.hover { background-color:#eee; } .my_clip_button.active { background-color:#aaa; }
</style>

<div id="d_clip_container" style="position:relative"> <div id="d_clip_button" class="my_clip_button"><b>Copy To Clipboard...</b></div> </div>

<script language="JavaScript">
        ZeroClipboard.setMoviePath('/ZeroClipboard/ZeroClipboard.swf');
        var clip = new ZeroClipboard.Client();
        clip.setText( 'Copy me!' );
        clip.setHandCursor( true );
        clip.glue( 'd_clip_button', 'd_clip_container' );
</script>