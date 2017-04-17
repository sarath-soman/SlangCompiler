function void main()

    var add = adder();
    var result = add(10, 10);
    println result;

    var number = num();

    println number();

    hello();

end

function (int,int)->int adder()
    var z = 10;
    var x = lambda int (int x, int y)
                println x;
                println y;
                return ( x + y ) * z;
            endlambda
    return x;
end

function ()->int num()
    return lambda int ()
                return 1;
            endlambda
end

function ()->void hello()
    return lambda void ()
                println "Hello";
            endlambda
end