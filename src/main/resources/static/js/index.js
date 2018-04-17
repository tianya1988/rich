$(document).ready(function () {
    var ids = undefined;
    var id = getParam('id');
    if(""!=id){
        $.ajax({
            type: "GET",
            url: "/goods/" + id + "/gift-id",
            cache: false,
            async: false,
            dataType: 'json',
            success: function (data) {
                ids = [id];
                data.ids.forEach(function(value){
                    ids.push(value)
                })
                console.log(data.ids)
            }
        });
    }

    if(undefined == ids){
        ids = ['756016', '756056', '756041','756047'];
    }

    ids.forEach(function (value, i) {
        var div = document.createElement("div");
        div.setAttribute("width","875px")
        div.setAttribute("height",'400px');
        div.setAttribute("margin",'0 auto');
        div.setAttribute("id",value);
        document.body.appendChild(div);
        $.ajax({
            type: "GET",
            url: "/goods/" + value + "/prices",
            cache: false,
            async: false,
            dataType: 'json',
            success: function (data) {
                var p1 = new PackageDetail();
                p1.name = value;
                p1.data = data.data.price_history
                freshDiv(value,[p1])
                console.log(p1)
            }
        });
    })
});

function PackageDetail(name, data) {
    this.name = name;
    this.data = data;
}

function freshDiv(divId,packageDetailArr){
    Highcharts.setOptions({ global: { useUTC: false } });
    var chart = Highcharts.chart(divId, {
        chart: {
            type: 'spline'
        },
        title: {
            text: divId
        },
        subtitle: {
            text: 'jason-zhang'
        },
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
                hour: '%Y-%m-%d<br/>%H:%M'
            },
            title: {
                text: 'Date'
            }
        },
        yAxis: {
            title: {
                text: '美元 ($)'
            },
            min: 0
        },
        tooltip: {
            headerFormat: '<b>{series.name}</b><br>',
            pointFormat: '{point.x:%H:%M}: {point.y:.2f} $'
        },
        plotOptions: {
            spline: {
                marker: {
                    enabled: true
                }
            }
        },
        series: packageDetailArr
    });
}

function getParam(paramName) {
    paramValue = "", isFound = !1;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}

