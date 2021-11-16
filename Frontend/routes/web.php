<?php

use App\Http\Controllers\BatchController;
use App\Http\Controllers\InfoController;
use App\Http\Controllers\ReportController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create.blade.php something great!
|
*/
//index
Route::get('/', [BatchController::class, "index"])->name("index");

Route::get('/configuration', [BatchController::class, "config"])->name("batch.config");
//batches
Route::get('batch/create', [BatchController::class, "create"])->name("batch.create");
Route::post('batch/create', [BatchController::class, "store"])->name("batch.store");

//reports
Route::get('/report', [ReportController::class, "showReport"])->name("report.show");
//Route::get("batch/{id}/report", [batchController::class, "showReport"])->name("batch.showReport");


