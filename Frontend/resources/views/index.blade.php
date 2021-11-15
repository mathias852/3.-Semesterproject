@foreach($batches as $batch)
    <li>
        <label for="batchId">Batch: {{$batch->id}}</label>
        <label for="batchAmount">Amount: {{$batch->amount}}</label>
    </li>
@endforeach
<h2>Report</h2>

<form action="{{route('report.view')}}" method="GET">

    <label for="batchId">Enter the batchid for the report
        <input type="number" name="batchId" required>
        @if($errors->has('batchId'))
{{--            {{dd($errors->first())}}--}}
            <p>{{($errors->first())}}</p>
        @endif

    </label>
    <button type="submit">Search</button>
</form>
<br>
<a href="{{route('batch.create')}}">
    <button>Make new Batch</button>
</a>
{{--{{dd($_POST)}}--}}
