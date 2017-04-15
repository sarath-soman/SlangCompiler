function void main()
    display(10);
end

function int display(int x)
    println x;
    if(x == 1) then return x; endif
    return display(x-1);
end