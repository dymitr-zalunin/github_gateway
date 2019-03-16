var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "1921",
        "ok": "1921",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "521",
        "ok": "521",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "9747",
        "ok": "9747",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "822",
        "ok": "822",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "843",
        "ok": "843",
        "ko": "-"
    },
    "percentiles1": {
        "total": "719",
        "ok": "719",
        "ko": "-"
    },
    "percentiles2": {
        "total": "745",
        "ok": "745",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1312",
        "ok": "1312",
        "ko": "-"
    },
    "percentiles4": {
        "total": "3754",
        "ok": "3754",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 1635,
        "percentage": 85
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 173,
        "percentage": 9
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 113,
        "percentage": 6
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "27.443",
        "ok": "27.443",
        "ko": "-"
    }
},
contents: {
"req_get-repository--78ae2": {
        type: "REQUEST",
        name: "get_repository_200_or403",
path: "get_repository_200_or403",
pathFormatted: "req_get-repository--78ae2",
stats: {
    "name": "get_repository_200_or403",
    "numberOfRequests": {
        "total": "1921",
        "ok": "1921",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "521",
        "ok": "521",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "9747",
        "ok": "9747",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "822",
        "ok": "822",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "843",
        "ok": "843",
        "ko": "-"
    },
    "percentiles1": {
        "total": "719",
        "ok": "719",
        "ko": "-"
    },
    "percentiles2": {
        "total": "745",
        "ok": "745",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1312",
        "ok": "1312",
        "ko": "-"
    },
    "percentiles4": {
        "total": "3754",
        "ok": "3754",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 1635,
        "percentage": 85
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 173,
        "percentage": 9
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 113,
        "percentage": 6
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "27.443",
        "ok": "27.443",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
