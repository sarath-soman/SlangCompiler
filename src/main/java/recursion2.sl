function void main()
    display(10, 0);
end

function int display(int x, int y)
    println x;
    println y;
    if(x == 1 || y == 9) then return 1; endif
    return display(x-1, y + 1);
end