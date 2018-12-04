using CSharpFunctionalExtensions;
using System;
using System.Collections.Generic;
using System.Text;

namespace SSE.Infrastructure.Dispatchers
{
    public interface ICommandHandler<T>
        where T : ICommand
    {
        Result Execute(T command);
    }
}
