function void main()

    var add = adder();
    var result = add(10, 10);
    println result;

    var number = num();

    var n = number();
    println n;

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
    var re = lambda int ()
                return 1;
            endlambda
    return re;
end

function ()->void hello()
    var re = lambda void ()
                println "Hello";
            endlambda
    return re;
end