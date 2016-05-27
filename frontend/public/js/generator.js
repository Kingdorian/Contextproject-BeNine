var address;
var cameracounter = 0;
var blockcounter = 0;
var presetcounter = 0;

// Document is ready, we can now manipulate it.
$(document).ready(function() {

    // Update server status every 10 seconds.
    setInterval(setServerStatus, 10 * 1000);

    // Generate the camera block area.
    generateCameraArea();

	// Load the available cameras.
	loadCameras();

    // Generate the presets area.
    generatePresets();

    console.log('Page has loaded successfully.');
});

/**
 * Sets the backend server status.
 */
function setServerStatus() {
    var statuslabel = $('#server_status')

    $.get("/api/getinfo", function (data) {
        if (data.backend.status === "online") {
			if(!statuslabel.hasClass("label-success")){
				loadCameras();
			}
            statuslabel.attr('class', 'label label-success');
        } else {
            statuslabel.attr('class', 'label label-danger');
        }
    }).fail(function () {
        statuslabel.attr('class', 'label label-danger');
    })

}

/**
 * Generates the camera area of the app.
 */
function generateCameraArea() {
    var carousel;
    carousel = $('#carousel');


    for (var i = 0; i < 4; i++) {
        addCameraBlock();
    }

    // This part is the carousel initialization.

    // Set first indicator active.
    carousel.find(".carousel-indicators").children().eq(0).attr('class', 'active');
    // Set first camera block active.
    carousel.find(".carousel-inner").children().eq(0).attr('class', 'item active');
}

/**
 * Adds a block of 4 camera's to select to the app.
 */
function addCameraBlock() {
    var carousel, indicators, block;

    carousel = $('#carousel');
    indicators = carousel.find(".carousel-indicators");

    block = $('<div class="item"></div>');
    indicators.append('<li data-target="#carousel-example-generic" data-slide-to=' + blockcounter + '></li>');
    blockcounter++;

    for (var i = 0; i < 2; i++) {
        addCameraRow(block);
    }

    $('.carousel-inner').append(block);

}

/**
 * Adds a row of two camera's to a block.
 * @param block
 */
function addCameraRow(block) {
    var row, camera_image, camera_element, camera_title;

    row = $('<div class="row">');

    for (var i = 0; i < 2; i++) {
        cameracounter++;
        camera_element = $('<div class="col-xs-6"></div>');
        camera_element.attr("id", "camera_" + cameracounter);

        camera_title = $('<div class="camera_title"></div>');
        camera_status = $('<div class="camera_status"></div>');
        camera_icon = $('<span class="glyphicon glyphicon-facetime-video" aria-hidden="true"></span>');
        camera_title_text = $('<span id="camera_title">' + cameracounter + '</span>');

        camera_title.append(camera_icon, camera_title_text);

        camera_image = $('<img data-src="holder.js/246x144?auto=yes&text=Camera ' + cameracounter + '&bg=8b8b8b" >').get(0);

    		camera_element.click(function() {
    			var camera_nr = $(this).attr('camera_number');
    			if ( camera_nr != undefined) {
    				setCurrentCamera(camera_nr);
    			}
    		});

        camera_element.append(camera_image, camera_title, camera_status);
        row.append(camera_element);
    }

    block.append(row);
}

/**
 * Generates the presets area of the app.
 */
function generatePresets() {
    for (var i = 0; i < 4; i++) {
        addPresetRow();
    }
}

/**
 * Adds a row to the presets area of the app.
 */
function addPresetRow() {
    var preset_row, preset_column;

    preset_row = $('<div class="row"></div>');

    // Generate four columns.
    for (var i = 0; i < 4; i++) {
        preset_column = $('<div onclick="presetcall($(this))"></div>');
        preset_row.append(preset_column);
    }

    // Now for each column add the preset block.
    preset_row.children().each( function(index, elem) {
        presetcounter++;
        $(elem).attr("class", "col-xs-3 none");
		$(elem).attr("id", "preset_" + presetcounter);
        addPreset(elem);
    });

    // Now append the preset row.
    $("#preset_area").append(preset_row);
}


/**
 * Adds a preset to the specified preset element row.
 * @param elem  A preset_row element.
 */
function addPreset(elem) {
    var preset_image, preset_caption;

    preset_image = $('<img onclick="presetcall($(this))" data-src="holder.js/128x77?auto=yes&text=Preset ' + presetcounter + '&bg=8b8b8b">').get(0);

    preset_caption = $('<h5>Preset ' + presetcounter + '&nbsp;&nbsp;&nbsp;&nbsp;</h5>');
	
	preset_edit = $('<span id="preset_edit" class="glyphicon glyphicon-pencil" style="cursor:pointer" aria-hidden="true" onclick="presetedit($(this))"></span>');
			
	preset_caption.append(preset_edit);

    $(elem).append(preset_image, preset_caption);
}
