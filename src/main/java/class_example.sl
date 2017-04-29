type Contact
    int num;
    string name;
endtype

function void main()
    var contact = new Contact();

    contact.name = "Sarath";
    contact.num = 9995189217;

    printContact(contact);

end

function void printContact(contact)
    println(contact.name);
    println(contact.num);
end