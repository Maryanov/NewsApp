package com.may.newsapplication.extension

private const val start_day=8
private const val end_day=10
private const val start_month=5
private const val end_month=7
private const val start_year=0
private const val end_year=4
private const val start_time=11
private const val end_time=16


fun String.formatDateTime() : String {

    return this.subSequence(start_day, end_day).toString()+ "." +
            this.subSequence(start_month, end_month).toString()+ "."+
            this.subSequence(start_year, end_year).toString()+"г."+"  "+
            this.subSequence(start_time, end_time).toString()
}

fun String.adaptUrl() : String{
    return this.replace("http://", "https://")
}

fun String.convertDateEngToRus() : String {

    return ""
}