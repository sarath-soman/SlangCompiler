function (int,int)->int adder()
    var z = 10;
    var x = lambda int (int x, int y)
                println x;
                println y;
                return ( x + y ) * z;
            endlambda
    return x;
end

function void main()
    var fn = adder();
    var result = fn(10, 10);
    println result;
end